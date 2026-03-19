package com.uade.tpo.marketplacePerfume.entity.dto;

import lombok.Data;

@Data
public class PerfumeDTO {
    private int id;
    private String name;
    private String brand;
    private String line;
    private String description;
    private int releaseYear;
}
