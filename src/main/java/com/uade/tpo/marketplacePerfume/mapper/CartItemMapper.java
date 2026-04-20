package com.uade.tpo.marketplacePerfume.mapper;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.CartItem;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemResponse;

public final class CartItemMapper {

    private CartItemMapper() {
    }

    public static CartItemResponse toResponse(CartItem entity) {
        if (entity == null) return null;
        CartItemResponse dto = new CartItemResponse();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setAddedAt(entity.getAddedAt());
        if (entity.getSample() != null) {
            dto.setSampleId(entity.getSample().getId());
        }
        return dto;
    }

    public static List<CartItemResponse> toResponseList(List<CartItem> entities) {
        List<CartItemResponse> dtos = new ArrayList<>();
        if (entities != null) {
            for (CartItem entity : entities) {
                dtos.add(toResponse(entity));
            }
        }
        return dtos;
    }
}
