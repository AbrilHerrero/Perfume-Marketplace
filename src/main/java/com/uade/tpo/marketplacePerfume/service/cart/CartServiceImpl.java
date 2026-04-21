package com.uade.tpo.marketplacePerfume.service.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplacePerfume.entity.Cart;
import com.uade.tpo.marketplacePerfume.entity.CartItem;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
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
import com.uade.tpo.marketplacePerfume.exceptions.sample.SampleNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.CartItemMapper;
import com.uade.tpo.marketplacePerfume.mapper.CartMapper;
import com.uade.tpo.marketplacePerfume.repository.CartItemRepository;
import com.uade.tpo.marketplacePerfume.repository.CartRepository;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;
import com.uade.tpo.marketplacePerfume.service.IOrderService;

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

    /**
     * Converts the buyer's cart into an Order.
     *
     * Steps:
     *   1. Load cart and validate it has items.
     *   2. Map CartItems to OrderItemCreateDTOs.
     *   3. Create the order (validates stock, decrements atomically, snapshots prices).
     *   4. Clear the cart.
     *
     * Future steps (when Payment and Shipment CRUDs are ready):
     *   5. TODO: Create a Payment (PENDING) linked to the order via PaymentService.
     *      - POST /payment with orderId will initiate payment processing.
     *      - On confirmation, update Payment status to COMPLETED and Order status to PAID.
     *   6. TODO: Create a Shipment (PENDING) linked to the order + buyer's address via ShipmentService.
     *      - POST /shipment with orderId will initiate the shipment.
     *      - Update Shipment status through SHIPPED -> IN_TRANSIT -> DELIVERED.
     *      - When Shipment reaches DELIVERED, update Order status to DELIVERED.
     */
    @Override
    @Transactional
    public OrderResponseDTO checkout(User user) {
        Cart cart = cartRepository.findByBuyer_Id(user.getId())
                .orElseThrow(EmptyCartException::new);

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new EmptyCartException();
        }

        List<OrderItemCreateDTO> items = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItemCreateDTO itemDto = new OrderItemCreateDTO();
                    itemDto.setSampleId(cartItem.getSample().getId());
                    itemDto.setQuantity(cartItem.getQuantity());
                    return itemDto;
                })
                .collect(Collectors.toList());

        OrderCreateDTO orderDto = new OrderCreateDTO();
        orderDto.setItems(items);

        OrderResponseDTO order = orderService.createOrder(orderDto, user);

        clearCart(user);

        // TODO Step 5: paymentService.createPayment(order.getId(), user);
        // TODO Step 6: shipmentService.createShipment(order.getId(), user);

        return order;
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
