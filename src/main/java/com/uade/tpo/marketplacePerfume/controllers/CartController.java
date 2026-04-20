package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.Cart;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.CartResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemAdd;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemUpdate;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderItemCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.cartItem.EmptyCartException;
import com.uade.tpo.marketplacePerfume.repository.CartRepository;
import com.uade.tpo.marketplacePerfume.service.IOrderService;
import com.uade.tpo.marketplacePerfume.service.cart.ICartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCart(user));
    }

    @GetMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> getCartItem(@AuthenticationPrincipal User user,
            @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.getCartItem(user, cartItemId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addCartItem(@AuthenticationPrincipal User user,
            @Valid @RequestBody CartItemAdd cartItemAdd) {
        CartItemResponse created = cartService.addCartItem(user, cartItemAdd);
        return ResponseEntity.created(URI.create("/cart/items/" + created.getId())).body(created);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItemQuantity(@AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemUpdate body) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(user, cartItemId, body.getQuantity()));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@AuthenticationPrincipal User user,
            @PathVariable Long cartItemId) {
        cartService.removeCartItem(user, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }

    /**
     * Checkout: converts the authenticated user's cart into an Order.
     *
     * Current flow:
     *   1. Load the user's cart and validate it has items.
     *   2. Build an OrderCreateDTO from cart items.
     *   3. Create the order (validates stock, decrements it atomically, snapshots unit prices).
     *   4. Clear the cart.
     *   5. Return the created order.
     *
     * Future steps (add here when Payment and Shipment CRUDs are ready):
     *   6. TODO: Create a Payment (PENDING) linked to the new order via PaymentService.
     *      - POST /payment with orderId will initiate payment processing.
     *      - On payment confirmation, update Payment status to COMPLETED and Order status to PAID.
     *   7. TODO: Create a Shipment (PENDING) linked to the order + buyer's address via ShipmentService.
     *      - POST /shipment with orderId will initiate the shipment.
     *      - Update Shipment status through SHIPPED → IN_TRANSIT → DELIVERED as tracking progresses.
     *      - When Shipment reaches DELIVERED, update Order status to DELIVERED.
     */
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout(@AuthenticationPrincipal User user) {
        // Step 1: load cart
        Cart cart = cartRepository.findByBuyer_Id(user.getId())
                .orElseThrow(EmptyCartException::new);

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new EmptyCartException();
        }

        // Step 2: build OrderCreateDTO from cart items
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

        // Step 3: create order (stock validation + atomic decrement happens inside)
        OrderResponseDTO order = orderService.createOrder(orderDto, user);

        // Step 4: clear cart after successful order
        cartService.clearCart(user);

        // TODO Step 6: create Payment (PENDING) for the order
        // PaymentResponseDTO payment = paymentService.createPayment(order.getId(), user);

        // TODO Step 7: create Shipment (PENDING) for the order + buyer address
        // ShipmentResponseDTO shipment = shipmentService.createShipment(order.getId(), user);

        // Step 5: return created order
        return ResponseEntity.created(URI.create("/order/" + order.getId())).body(order);
    }
}