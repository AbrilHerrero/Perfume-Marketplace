package com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.PerfumeNotes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PerfumeCreateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    private String line;

    @NotBlank
    private String description;

    @NotNull
    @Min(1900)
    private Integer releaseYear;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String gender;

    @NotBlank
    private String sillage;

    @NotNull
    @Valid
    private PerfumeNotes notes;

    @NotEmpty
    private List<String> accords;
}
