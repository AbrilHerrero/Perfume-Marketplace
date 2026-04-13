package com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SampleRequestDTO {
    private int volumeMl;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private int stock;
    private Long perfumeId; // Recibimos el ID
    private Long sellerId;  // Recibimos el ID
}
