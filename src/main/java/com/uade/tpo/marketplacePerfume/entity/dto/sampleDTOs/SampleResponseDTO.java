package com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SampleResponseDTO {
    private Long id;
    private int volumeMl;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private int stock;
    private String perfumeName; // Aplana el dato para evitar anidar objetos completos en la respuesta
    private String brand;       // Aplana el dato para evitar anidar objetos completos en la respuesta
    private String sellerName;  // Aplana el dato para evitar anidar objetos completos en la respuesta
}
