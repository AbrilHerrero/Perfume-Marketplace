package com.uade.tpo.marketplacePerfume.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Cart;
import com.uade.tpo.marketplacePerfume.entity.CartItem;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.CartResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemResponse;

public final class CartMapper {

    private CartMapper() {
    }

    public static CartResponse toResponse(Cart entity) {
        if (entity == null) return null;
        CartResponse dto = new CartResponse();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        List<CartItem> items = entity.getCartItems();
        List<CartItemResponse> itemResponses = CartItemMapper.toResponseList(items);
        dto.setItems(itemResponses);
        dto.setTotalPrice(calculateTotalPrice(items));
        return dto;
    }

    private static BigDecimal calculateTotalPrice(List<CartItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        if (items == null) return total;
        for (CartItem item : items) {
            Sample sample = item.getSample();
            if (sample == null || sample.getPrice() == null) continue;
            total = total.add(sample.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }
}
