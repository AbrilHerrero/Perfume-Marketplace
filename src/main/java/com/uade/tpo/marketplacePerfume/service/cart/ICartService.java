package com.uade.tpo.marketplacePerfume.service.cart;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.CartResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemAdd;

public interface ICartService {
    CartResponse getCart(User user);
    CartItemResponse getCartItem(User user, Long cartItemId);
    CartItemResponse addCartItem(User user, CartItemAdd cartItemAdd);
    CartItemResponse updateCartItemQuantity(User user, Long cartItemId, Integer quantity);
    void removeCartItem(User user, Long cartItemId);
    void clearCart(User user);
}
