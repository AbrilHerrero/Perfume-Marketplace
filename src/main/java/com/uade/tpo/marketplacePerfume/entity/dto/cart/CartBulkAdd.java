package com.uade.tpo.marketplacePerfume.entity.dto.cart;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemAdd;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CartBulkAdd {

    @NotEmpty
    @Valid
    private List<CartItemAdd> items;
}