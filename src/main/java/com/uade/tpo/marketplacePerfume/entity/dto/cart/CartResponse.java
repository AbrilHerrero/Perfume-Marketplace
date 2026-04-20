package com.uade.tpo.marketplacePerfume.entity.dto.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemResponse;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponse {
    private Long id;
    private List<CartItemResponse> items;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
