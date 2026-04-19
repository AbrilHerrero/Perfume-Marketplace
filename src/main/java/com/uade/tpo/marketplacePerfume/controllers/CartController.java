package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;
import java.util.List;

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

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.CartResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.CartBulkAdd;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemAdd;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemUpdate;
import com.uade.tpo.marketplacePerfume.service.cart.ICartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

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

    @PostMapping("/items/bulk")
    public ResponseEntity<List<CartItemResponse>> addCartItems(@AuthenticationPrincipal User user,
            @Valid @RequestBody CartBulkAdd cartBulkAdd) {
        List<CartItemResponse> created = cartService.addCartItems(user, cartBulkAdd);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(@AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemUpdate body) {
        cartService.updateCartItemQuantity(user, cartItemId, body.getQuantity());
        return ResponseEntity.noContent().build();
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
}