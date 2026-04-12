package com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemResponseDTO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponseDTO {

    private Long id;
    private List<CartItemResponseDTO> items;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
