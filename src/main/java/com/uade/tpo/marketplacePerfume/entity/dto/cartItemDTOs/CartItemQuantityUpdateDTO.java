package com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemQuantityUpdateDTO {

    @Min(1)
    private int quantity;
}
