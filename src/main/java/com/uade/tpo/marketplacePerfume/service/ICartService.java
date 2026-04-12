package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartBulkAddDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartStockCheckResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemAddDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemResponseDTO;

public interface ICartService {

    CartResponseDTO getCart(User user);

    CartItemResponseDTO addCartItem(User user, CartItemAddDTO request);

    List<CartItemResponseDTO> addCartItems(User user, CartBulkAddDTO request);

    CartItemResponseDTO getCartItem(User user, Long cartItemId);

    void updateCartItemQuantity(User user, Long cartItemId, int quantity);

    void removeCartItem(User user, Long cartItemId);

    void clearCart(User user);

    CartStockCheckResponseDTO checkCartStock(User user);

    // Checkout: pendiente — exponer POST /cart/checkout (o POST /orders) al implementar Order
    // y crear la orden desde el carrito del comprador actual.
}
