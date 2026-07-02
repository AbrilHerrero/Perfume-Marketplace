package com.uade.tpo.marketplacePerfume.service.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplacePerfume.entity.Cart;
import com.uade.tpo.marketplacePerfume.entity.CartItem;
import com.uade.tpo.marketplacePerfume.entity.Coupon;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.ApplyCouponResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.CartResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemAdd;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderItemCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.cartItem.CartItemInsufficientStockException;
import com.uade.tpo.marketplacePerfume.exceptions.cartItem.CartItemInvalidQuantityException;
import com.uade.tpo.marketplacePerfume.exceptions.cartItem.CartItemNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.cartItem.EmptyCartException;
import com.uade.tpo.marketplacePerfume.exceptions.coupon.CouponNotApplicableException;
import com.uade.tpo.marketplacePerfume.exceptions.sample.SampleNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.CartItemMapper;
import com.uade.tpo.marketplacePerfume.mapper.CartMapper;
import com.uade.tpo.marketplacePerfume.repository.CartItemRepository;
import com.uade.tpo.marketplacePerfume.repository.CartRepository;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;
import com.uade.tpo.marketplacePerfume.service.coupon.ICouponService;
import com.uade.tpo.marketplacePerfume.service.order.IOrderService;
import com.uade.tpo.marketplacePerfume.service.payment.IPaymentService;
import com.uade.tpo.marketplacePerfume.service.shipment.IShipmentService;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IShipmentService shipmentService;

    @Autowired
    private ICouponService couponService;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(User user) {
        return cartRepository.findByBuyer_Id(user.getId())
                .map(CartMapper::toResponse)
                .orElseGet(CartServiceImpl::emptyCartResponse);
    }

    private static CartResponse emptyCartResponse() {
        CartResponse dto = new CartResponse();
        dto.setItems(Collections.emptyList());
        dto.setTotalPrice(BigDecimal.ZERO);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public CartItemResponse getCartItem(User user, Long cartItemId) {
        Cart cart = findCart(user, CartItemNotFoundException::new);
        CartItem item = findOwnedCartItem(cart, cartItemId);
        return CartItemMapper.toResponse(item);
    }

    @Override
    @Transactional
    public CartItemResponse addCartItem(User user, CartItemAdd cartItemAdd) {
        LocalDateTime now = LocalDateTime.now();
        Cart cart = getOrCreateCart(user, now);
        CartItem saved = addOrMergeItem(cart, cartItemAdd.getSampleId(),
                requireOrderedQuantity(cartItemAdd.getQuantity()), now);
        touchCart(cart, now);
        return CartItemMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItemQuantity(User user, Long cartItemId, Integer quantity) {
        LocalDateTime now = LocalDateTime.now();
        int q = requireOrderedQuantity(quantity);
        Cart cart = findCart(user, CartItemNotFoundException::new);
        CartItem item = findOwnedCartItem(cart, cartItemId);
        Sample sample = item.getSample();
        requireActiveSample(sample);
        validateStock(sample, q);
        item.setQuantity(q);
        touchCart(cart, now);
        return CartItemMapper.toResponse(item);
    }

    @Override
    @Transactional
    public void removeCartItem(User user, Long cartItemId) {
        LocalDateTime now = LocalDateTime.now();
        Cart cart = findCart(user, CartItemNotFoundException::new);
        CartItem item = findOwnedCartItem(cart, cartItemId);
        cartItemRepository.delete(item);
        if (cartItemRepository.existsByCart_Id(cart.getId())) {
            touchCart(cart, now);
        } else {
            deleteCart(cart);
        }
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        cartRepository.findByBuyer_Id(user.getId()).ifPresent(cart -> {
            cartItemRepository.deleteAllByCart_Id(cart.getId());
            deleteCart(cart);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public ApplyCouponResponse applyCoupon(User user, String couponCode) {
        Cart cart = cartRepository.findByBuyer_IdWithItems(user.getId())
                .orElseThrow(EmptyCartException::new);

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new EmptyCartException();
        }

        Coupon coupon = couponService.validateForRedemption(couponCode.trim(), user);

        Map<Long, BigDecimal> subtotalsBySeller = subtotalBySeller(cart.getCartItems());
        BigDecimal cartSubtotal = subtotalsBySeller.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sellerSubtotal = subtotalsBySeller.getOrDefault(
                coupon.getSeller().getId(), BigDecimal.ZERO);

        if (sellerSubtotal.signum() == 0) {
            throw new CouponNotApplicableException();
        }

        BigDecimal discountAmount = couponService.computeDiscount(coupon, sellerSubtotal);

        ApplyCouponResponse response = new ApplyCouponResponse();
        response.setCode(coupon.getCode());
        response.setDiscountType(coupon.getDiscountType());
        response.setDiscountValue(coupon.getDiscountValue());
        response.setSellerId(coupon.getSeller().getId());
        response.setCartSubtotal(cartSubtotal);
        response.setSellerSubtotal(sellerSubtotal);
        response.setDiscountAmount(discountAmount);
        response.setTotalAfterDiscount(cartSubtotal.subtract(discountAmount));
        return response;
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> checkout(User user, String couponCode) {
        Cart cart = cartRepository.findByBuyer_Id(user.getId())
                .orElseThrow(EmptyCartException::new);

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new EmptyCartException();
        }

        // A cart can hold items from several sellers, but each seller ships and
        // tracks their own decants. So the checkout splits into one order per
        // seller — each with its own payment and shipment — instead of a single
        // mixed order that no seller could fully own. A LinkedHashMap keeps the
        // orders in the sellers' first-seen order for a deterministic result.
        Map<Long, List<CartItem>> itemsBySeller = new LinkedHashMap<>();
        for (CartItem cartItem : cart.getCartItems()) {
            Sample sample = cartItem.getSample();
            if (sample == null || sample.getSeller() == null) {
                throw new SampleNotFoundException();
            }
            itemsBySeller
                    .computeIfAbsent(sample.getSeller().getId(), k -> new ArrayList<>())
                    .add(cartItem);
        }

        // A coupon belongs to a single seller, so only that seller's order
        // carries it; the rest check out at full price. Resolve (and validate)
        // the owner up front so an invalid coupon — or one whose seller isn't in
        // the cart — fails before any order is created.
        Long couponSellerId = null;
        if (couponCode != null && !couponCode.isBlank()) {
            Coupon coupon = couponService.validateForRedemption(couponCode.trim(), user);
            couponSellerId = coupon.getSeller().getId();
            if (!itemsBySeller.containsKey(couponSellerId)) {
                throw new CouponNotApplicableException();
            }
        }

        List<OrderResponseDTO> orders = new ArrayList<>();
        for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {
            List<OrderItemCreateDTO> items = entry.getValue().stream()
                    .map(cartItem -> {
                        OrderItemCreateDTO itemDto = new OrderItemCreateDTO();
                        itemDto.setSampleId(cartItem.getSample().getId());
                        itemDto.setQuantity(cartItem.getQuantity());
                        return itemDto;
                    })
                    .collect(Collectors.toList());

            OrderCreateDTO orderDto = new OrderCreateDTO();
            orderDto.setItems(items);
            orderDto.setCouponCode(entry.getKey().equals(couponSellerId) ? couponCode : null);

            OrderResponseDTO order = orderService.createOrder(orderDto, user);
            paymentService.create(order.getId(), user);
            shipmentService.create(order.getId(), user);
            orders.add(order);
        }

        clearCart(user);

        return orders;
    }

    private Map<Long, BigDecimal> subtotalBySeller(List<CartItem> cartItems) {
        Map<Long, BigDecimal> subtotals = new HashMap<>();
        if (cartItems == null) {
            return subtotals;
        }

        for (CartItem item : cartItems) {
            Sample sample = item.getSample();
            if (sample == null || sample.getPrice() == null || sample.getSeller() == null) {
                continue;
            }
            BigDecimal lineTotal = sample.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotals.merge(sample.getSeller().getId(), lineTotal, BigDecimal::add);
        }
        return subtotals;
    }

    private Cart getOrCreateCart(User user, LocalDateTime now) {
        return cartRepository.findByBuyer_Id(user.getId())
                .orElseGet(() -> createCartFor(user.getId(), now));
    }

    private Cart findCart(User user, Supplier<? extends RuntimeException> notFoundSupplier) {
        return cartRepository.findByBuyer_Id(user.getId())
                .orElseThrow(notFoundSupplier);
    }

    private Cart createCartFor(Long buyerId, LocalDateTime now) {
        Cart cart = Cart.builder()
                .buyer(userRepository.getReferenceById(buyerId))
                .createdAt(now)
                .updatedAt(now)
                .build();
        return cartRepository.save(cart);
    }

    private CartItem findOwnedCartItem(Cart cart, Long cartItemId) {
        return cartItemRepository.findByIdAndCart_Id(cartItemId, cart.getId())
                .orElseThrow(CartItemNotFoundException::new);
    }

    private CartItem addOrMergeItem(Cart cart, Long sampleId, int quantity, LocalDateTime now) {
        Sample sample = sampleRepository.findById(sampleId)
                .orElseThrow(SampleNotFoundException::new);
        requireActiveSample(sample);

        return cartItemRepository.findByCart_IdAndSample_Id(cart.getId(), sampleId)
                .map(existing -> {
                    int newQuantity = existing.getQuantity() + quantity;
                    validateStock(sample, newQuantity);
                    existing.setQuantity(newQuantity);
                    return existing;
                })
                .orElseGet(() -> {
                    validateStock(sample, quantity);
                    CartItem newItem = CartItem.builder()
                            .cart(cart)
                            .sample(sample)
                            .quantity(quantity)
                            .addedAt(now)
                            .build();
                    return cartItemRepository.save(newItem);
                });
    }

    private void requireActiveSample(Sample sample) {
        if (sample == null || !sample.isActive()) {
            throw new SampleNotFoundException();
        }
    }

    private int requireOrderedQuantity(Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new CartItemInvalidQuantityException();
        }
        return quantity;
    }

    private void validateStock(Sample sample, int quantity) {
        if (quantity < 1) {
            throw new CartItemInvalidQuantityException();
        }
        if (sample == null || sample.getStock() < quantity) {
            throw new CartItemInsufficientStockException();
        }
    }

    private void touchCart(Cart cart, LocalDateTime now) {
        cart.setUpdatedAt(now);
    }

    private void deleteCart(Cart cart) {
        User buyer = cart.getBuyer();
        if (buyer != null) {
            buyer.setCart(null);
        }
        cartRepository.delete(cart);
    }
}
