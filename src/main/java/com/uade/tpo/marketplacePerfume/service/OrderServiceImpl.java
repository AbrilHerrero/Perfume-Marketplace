package com.uade.tpo.marketplacePerfume.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.OrderItem;
import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderItemCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderStatusUpdateDTO;
import com.uade.tpo.marketplacePerfume.exceptions.InsufficientStockException;
import com.uade.tpo.marketplacePerfume.exceptions.InvalidOrderStatusException;
import com.uade.tpo.marketplacePerfume.exceptions.OrderAccessDeniedException;
import com.uade.tpo.marketplacePerfume.exceptions.OrderNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.SampleNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.OrderMapper;
import com.uade.tpo.marketplacePerfume.repository.OrderItemRepository;
import com.uade.tpo.marketplacePerfume.repository.OrderRepository;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;

@Service
public class OrderServiceImpl implements IOrderService {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_CANCELLED = "CANCELLED";
    private static final Set<String> ALLOWED_STATUSES = Set.of(
            STATUS_PENDING, "PAID", "SHIPPED", "DELIVERED", STATUS_CANCELLED);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return OrderMapper.toResponseDtoList(orderRepository.findAll());
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
                .status(STATUS_PENDING)
                .total(BigDecimal.ZERO)
                .orderItems(new ArrayList<>())
                .build();
        Order savedOrder = orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;
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

            total = total.add(sample.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
        }

        savedOrder.setOrderItems(persistedItems);
        savedOrder.setTotal(total);
        Order finalOrder = orderRepository.save(savedOrder);

        return OrderMapper.toResponseDto(finalOrder);
    }

    @Override
    @Transactional
    public OrderResponseDTO updateStatus(Long id, OrderStatusUpdateDTO dto) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        String newStatus = dto.getStatus();
        if (newStatus == null || !ALLOWED_STATUSES.contains(newStatus)) {
            throw new InvalidOrderStatusException();
        }
        order.setStatus(newStatus);
        return OrderMapper.toResponseDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long id, User currentUser) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        assertCanAccess(order, currentUser);

        if (!STATUS_PENDING.equals(order.getStatus())) {
            throw new InvalidOrderStatusException();
        }

        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                if (item.getSample() != null) {
                    sampleRepository.incrementStock(item.getSample().getId(), item.getQuantity());
                }
            }
        }

        order.setStatus(STATUS_CANCELLED);
        return OrderMapper.toResponseDto(orderRepository.save(order));
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
