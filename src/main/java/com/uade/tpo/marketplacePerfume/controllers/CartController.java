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
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartBulkAddDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartStockCheckResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemAddDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemQuantityUpdateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemResponseDTO;
import com.uade.tpo.marketplacePerfume.service.ICartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCart(user));
    }

    @GetMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponseDTO> getCartItem(@AuthenticationPrincipal User user,
            @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.getCartItem(user, cartItemId));
    }

    @GetMapping("/stock-check")
    public ResponseEntity<CartStockCheckResponseDTO> checkCartStock(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.checkCartStock(user));
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponseDTO> addCartItem(@AuthenticationPrincipal User user,
            @Valid @RequestBody CartItemAddDTO cartItemAddDTO) {
        CartItemResponseDTO created = cartService.addCartItem(user, cartItemAddDTO);
        return ResponseEntity.created(URI.create("/cart/items/" + created.getId())).body(created);
    }

    @PostMapping("/items/bulk")
    public ResponseEntity<List<CartItemResponseDTO>> addCartItems(@AuthenticationPrincipal User user,
            @Valid @RequestBody CartBulkAddDTO cartBulkAddDTO) {
        List<CartItemResponseDTO> created = cartService.addCartItems(user, cartBulkAddDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(@AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemQuantityUpdateDTO body) {
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
