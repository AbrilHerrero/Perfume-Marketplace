package com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemAddDTO {
    @NotNull
    private Long sampleId;

    @Min(1)
    private int quantity;
}
