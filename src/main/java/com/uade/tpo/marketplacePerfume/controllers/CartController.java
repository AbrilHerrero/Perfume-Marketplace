package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemAddDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemQuantityUpdateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartBulkAddDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartStockCheckResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderResponseDTO;
import com.uade.tpo.marketplacePerfume.service.ICartService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @GetMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponseDTO> getCartItem(@PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.getCartItem(cartItemId));
    }

    @PostMapping
    public ResponseEntity<CartItemResponseDTO> addCartItem(@Valid @RequestBody CartItemAddDTO cartItemAddDTO) {
        CartItemResponseDTO created = cartService.addCartItem(cartItemAddDTO);
        return ResponseEntity.created(URI.create("/cart/items/" + created.getId())).body(created);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<CartItemResponseDTO>> addCartItems(@Valid @RequestBody CartBulkAddDTO cartBulkAddDTO) {
        List<CartItemResponseDTO> created = cartService.addCartItems(cartBulkAddDTO);
        return ResponseEntity.created(URI.create("/cart/bulk")).body(created);
    }

    @GetMapping("/stock")
    public ResponseEntity<CartStockCheckResponseDTO> checkCartStock() {
        return ResponseEntity.ok(cartService.checkCartStock());
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkoutCart() {
        return ResponseEntity.ok(cartService.checkoutCart());
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<Void> updateCartItemQuantity(@PathVariable Long cartItemId,
            @Valid @RequestBody CartItemQuantityUpdateDTO body) {
        cartService.updateCartItemQuantity(cartItemId, body.getQuantity());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
