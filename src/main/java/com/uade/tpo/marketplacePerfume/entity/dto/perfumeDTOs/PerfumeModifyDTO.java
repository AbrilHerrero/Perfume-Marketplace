package com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs;

import lombok.Data;

@Data
public class PerfumeModifyDTO {
    private String name;
    private String brand;
    private Integer releaseYear;
    private String imageUrl;
    private String gender;
    private String sillage;
    private String confidence;
}
