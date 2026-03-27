package com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PerfumeCreateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    private Integer releaseYear;
    private String imageUrl;
    private String gender;
    private String sillage;
    private String confidence;
}
