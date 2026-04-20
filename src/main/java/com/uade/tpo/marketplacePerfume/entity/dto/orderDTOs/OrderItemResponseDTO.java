package com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemResponseDTO {

    private Long id;
    private Long sampleId;
    private String sampleDescription;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal subtotal;
}
