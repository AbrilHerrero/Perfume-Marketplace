package com.uade.tpo.marketplacePerfume.service.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.repository.CartRepository;
import com.uade.tpo.marketplacePerfume.repository.CartItemRepository;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.CartResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemAdd;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.CartBulkAdd;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartRepository cartRepository;   

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartResponse getCart(User user) {
        return null;
    }

    @Override
    public CartItemResponse getCartItem(User user, Long cartItemId) {
        return null;
    }

    @Override
    public CartItemResponse addCartItem(User user, CartItemAdd cartItemAdd) {
        return null;
    }

    @Override
    public List<CartItemResponse> addCartItems(User user, CartBulkAdd cartBulkAdd) {
        return null;
    }

    @Override
    public void updateCartItemQuantity(User user, Long cartItemId, int quantity) {
    }

    @Override
    public void removeCartItem(User user, Long cartItemId) {
    }

    @Override
    public void clearCart(User user) {
    }
}
