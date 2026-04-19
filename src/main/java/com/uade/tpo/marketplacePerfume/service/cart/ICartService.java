package com.uade.tpo.marketplacePerfume.service.cart;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.CartResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemAdd;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.CartBulkAdd;

public interface ICartService {
    CartResponse getCart(User user);
    CartItemResponse getCartItem(User user, Long cartItemId);
    CartItemResponse addCartItem(User user, CartItemAdd cartItemAdd);
    List<CartItemResponse> addCartItems(User user, CartBulkAdd cartBulkAdd);
    void updateCartItemQuantity(User user, Long cartItemId, int quantity);
    void removeCartItem(User user, Long cartItemId);
    void clearCart(User user);
}
