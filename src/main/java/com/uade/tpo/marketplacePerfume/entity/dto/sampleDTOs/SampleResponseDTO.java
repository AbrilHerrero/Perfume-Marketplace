package com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SampleResponseDTO {
    private Long id;
    private BigDecimal price;
    private Integer stock;
    private Integer volumeMl;
    private String description;
    private String imageUrl;
    private Long perfumeId; // Lo volvemos a agregar porque lo necesitas para crear la Sample
    private Long sellerId;  // Lo mismo para el vendedor
}
