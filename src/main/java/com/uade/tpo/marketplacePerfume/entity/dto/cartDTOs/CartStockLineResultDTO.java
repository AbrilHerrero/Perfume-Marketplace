package com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs;

import lombok.Data;

@Data
public class CartStockLineResultDTO {

    private Long cartItemId;
    private Long sampleId;
    private int requestedQuantity;
    private int availableStock;
    private boolean sufficient;
}
