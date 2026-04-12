package com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemAddDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CartBulkAddDTO {

    @NotEmpty
    @Valid
    private List<CartItemAddDTO> items;
}
