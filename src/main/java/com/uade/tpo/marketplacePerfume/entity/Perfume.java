package com.uade.tpo.marketplacePerfume.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Perfume {
    private int id;
    private String name;
    private String brand;
    private String line;
    private String description;
    private int releaseYear;
}
