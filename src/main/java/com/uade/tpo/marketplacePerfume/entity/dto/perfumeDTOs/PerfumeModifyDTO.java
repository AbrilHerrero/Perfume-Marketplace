package com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.PerfumeNotes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PerfumeModifyDTO {
    private String name;
    private String brand;
    private String line;
    private String description;

    @Min(1900)
    private Integer releaseYear;

    private String imageUrl;
    private String gender;
    private String sillage;

    @Valid
    private PerfumeNotes notes;

    private List<String> accords;
}
