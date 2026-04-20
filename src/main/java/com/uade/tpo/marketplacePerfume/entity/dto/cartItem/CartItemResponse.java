package com.uade.tpo.marketplacePerfume.entity.dto.cartItem;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemResponse {
    private Long id;
    private int quantity;
    private LocalDateTime addedAt;
    private Long sampleId;
}
