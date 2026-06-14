package com.uade.tpo.marketplacePerfume.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplacePerfume.entity.Coupon;
import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.OrderItem;
import com.uade.tpo.marketplacePerfume.entity.OrderStatus;
import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderItemCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderStatusUpdateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.SellerStatsResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.sample.SampleNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.coupon.CouponNotApplicableException;
import com.uade.tpo.marketplacePerfume.exceptions.order.InsufficientStockException;
import com.uade.tpo.marketplacePerfume.exceptions.order.InvalidOrderStatusException;
import com.uade.tpo.marketplacePerfume.exceptions.order.OrderAccessDeniedException;
import com.uade.tpo.marketplacePerfume.exceptions.order.OrderNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.OrderMapper;
import com.uade.tpo.marketplacePerfume.repository.OrderItemRepository;
import com.uade.tpo.marketplacePerfume.repository.OrderRepository;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;
import com.uade.tpo.marketplacePerfume.service.coupon.ICouponService;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private ICouponService couponService;

    private static final List<OrderStatus> SOLD_STATUSES =
            List.of(OrderStatus.PAID, OrderStatus.SHIPPED, OrderStatus.DELIVERED);

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return OrderMapper.toResponseDtoList(orderRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public SellerStatsResponseDTO getSellerStats(User seller) {
        LocalDateTime since = LocalDateTime.now().minusDays(30);
        long sold = orderItemRepository.sumQuantityBySellerSince(seller.getId(), SOLD_STATUSES, since);
        BigDecimal revenue = orderItemRepository.sumRevenueBySellerSince(seller.getId(), SOLD_STATUSES, since);
        return SellerStatsResponseDTO.builder()
                .soldLast30Days(sold)
                .revenueLast30Days(revenue != null ? revenue : BigDecimal.ZERO)
                .build();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByBuyer(Long buyerId) {
        return OrderMapper.toResponseDtoList(orderRepository.findByBuyerId(buyerId));
    }

    @Override
    public OrderResponseDTO getOrderById(Long id, User currentUser) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        assertCanAccess(order, currentUser);
        return OrderMapper.toResponseDto(order);
    }

    /**
     * Entry point for the future CartController#/cart/checkout flow.
     * Not exposed directly via OrderController — order creation goes through the cart.
     */
    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderCreateDTO dto, User buyer) {
        Order order = Order.builder()
                .buyer(buyer)
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .total(BigDecimal.ZERO)
                .orderItems(new ArrayList<>())
                .build();
        Order savedOrder = orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;
        Map<Long, BigDecimal> subtotalBySeller = new HashMap<>();
        List<OrderItem> persistedItems = new ArrayList<>();

        for (OrderItemCreateDTO itemDto : dto.getItems()) {
            Sample sample = sampleRepository.findById(itemDto.getSampleId())
                    .orElseThrow(SampleNotFoundException::new);

            int updated = sampleRepository.decrementStock(sample.getId(), itemDto.getQuantity());
            if (updated == 0) {
                throw new InsufficientStockException();
            }

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .sample(sample)
                    .unitPrice(sample.getPrice())
                    .quantity(itemDto.getQuantity())
                    .build();
            persistedItems.add(orderItemRepository.save(orderItem));

            BigDecimal lineTotal = sample.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            total = total.add(lineTotal);
            if (sample.getSeller() != null) {
                subtotalBySeller.merge(sample.getSeller().getId(), lineTotal, BigDecimal::add);
            }
        }

        Coupon coupon = null;
        BigDecimal discount = BigDecimal.ZERO;
        String couponCode = dto.getCouponCode();
        if (couponCode != null && !couponCode.isBlank()) {
            coupon = couponService.validateForRedemption(couponCode, buyer);
            BigDecimal sellerSubtotal = subtotalBySeller.getOrDefault(
                    coupon.getSeller().getId(), BigDecimal.ZERO);
            if (sellerSubtotal.signum() == 0) {
                throw new CouponNotApplicableException();
            }
            discount = couponService.computeDiscount(coupon, sellerSubtotal);
            total = total.subtract(discount);
            savedOrder.setCouponCode(coupon.getCode());
            savedOrder.setDiscountAmount(discount);
        }

        savedOrder.setOrderItems(persistedItems);
        savedOrder.setTotal(total);
        Order finalOrder = orderRepository.save(savedOrder);

        if (coupon != null) {
            couponService.recordRedemption(coupon, buyer, finalOrder, discount);
        }

        return OrderMapper.toResponseDto(finalOrder);
    }

    @Override
    @Transactional
    public OrderResponseDTO updateStatus(Long id, OrderStatusUpdateDTO dto) {
        Order order = orderRepository.findByIdWithBuyerAndItems(id).orElseThrow(OrderNotFoundException::new);
        OrderStatus previousStatus = order.getStatus();
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(dto.getStatus().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidOrderStatusException();
        }

        if (previousStatus != newStatus) {
            if (newStatus == OrderStatus.CANCELLED) {
                restoreStockForOrder(order);
            } else if (previousStatus == OrderStatus.CANCELLED) {
                reserveStockForOrder(order);
            }
        }

        order.setStatus(newStatus);
        return OrderMapper.toResponseDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long id, User currentUser) {
        Order order = orderRepository.findByIdWithBuyerAndItems(id).orElseThrow(OrderNotFoundException::new);
        assertCanAccess(order, currentUser);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException();
        }

        restoreStockForOrder(order);
        order.setStatus(OrderStatus.CANCELLED);
        return OrderMapper.toResponseDto(orderRepository.save(order));
    }

    private void restoreStockForOrder(Order order) {
        if (order.getOrderItems() == null) {
            return;
        }
        for (OrderItem item : order.getOrderItems()) {
            if (item.getSample() != null) {
                sampleRepository.incrementStock(item.getSample().getId(), item.getQuantity());
            }
        }
    }

    private void reserveStockForOrder(Order order) {
        if (order.getOrderItems() == null) {
            return;
        }
        for (OrderItem item : order.getOrderItems()) {
            if (item.getSample() != null) {
                int updated = sampleRepository.decrementStock(item.getSample().getId(), item.getQuantity());
                if (updated == 0) {
                    throw new InsufficientStockException();
                }
            }
        }
    }

    private void assertCanAccess(Order order, User currentUser) {
        if (currentUser == null) {
            throw new OrderAccessDeniedException();
        }
        if (currentUser.getRole() == Role.ADMIN) {
            return;
        }
        if (order.getBuyer() == null || !order.getBuyer().getId().equals(currentUser.getId())) {
            throw new OrderAccessDeniedException();
        }
    }
}
