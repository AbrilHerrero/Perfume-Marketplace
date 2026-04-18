package com.uade.tpo.marketplacePerfume.entity.dto.Sample;

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
    private Long perfumeId; 
    private Long sellerId;
}