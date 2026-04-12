package com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemResponseDTO {

    private Long id;
    private int quantity;
    private LocalDateTime addedAt;
    private CartSampleSummaryDTO sample;
}
