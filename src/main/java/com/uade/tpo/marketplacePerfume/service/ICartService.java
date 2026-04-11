package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartBulkAddDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartStockCheckResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemAddDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemResponseDTO;

public interface ICartService {

    CartResponseDTO getCart();

    CartItemResponseDTO addCartItem(CartItemAddDTO request);

    List<CartItemResponseDTO> addCartItems(CartBulkAddDTO request);

    CartItemResponseDTO getCartItem(Long cartItemId);
    
    void updateCartItemQuantity(Long cartItemId, int quantity);

    void removeCartItem(Long cartItemId);

    void clearCart();

    CartStockCheckResponseDTO checkCartStock();

    // Checkout: pendiente — exponer POST /cart/checkout (o POST /orders) al implementar Order
    // y crear la orden desde el carrito del comprador actual.
}
