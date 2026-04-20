package com.uade.tpo.marketplacePerfume.entity.dto.cartItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemAdd {
    @NotNull(message = "sampleId is required")
    private Long sampleId;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be greater than 0")
    private Integer quantity;
}
