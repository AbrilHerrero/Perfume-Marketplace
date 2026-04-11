package com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs;

import java.util.List;

import lombok.Data;

@Data
public class CartStockCheckResponseDTO {

    private boolean allSufficient;
    private List<CartStockLineResultDTO> lines;
}
