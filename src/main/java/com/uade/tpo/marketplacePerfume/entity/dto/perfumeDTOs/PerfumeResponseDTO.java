package com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.PerfumeNotes;

import lombok.Data;

@Data
public class PerfumeResponseDTO {
    private Long id;
    private String name;
    private String brand;
    private String line;
    private String description;
    private int releaseYear;
    private String imageUrl;
    private String gender;
    private String sillage;
    private PerfumeNotes notes;
    private List<String> accords;
}
