package com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs;

import lombok.Data;

@Data
public class PerfumeResponseDTO {
    private Long id;
    private String name;
    private String brand;
    private int releaseYear;
    private String imageUrl;
    private String gender;
    private String sillage;
    private String confidence;
}
