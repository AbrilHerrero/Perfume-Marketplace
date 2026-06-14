package com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.OccasionProfile;
import com.uade.tpo.marketplacePerfume.entity.PerfumeNotes;
import com.uade.tpo.marketplacePerfume.entity.SeasonProfile;

import lombok.Data;

@Data
public class PerfumeModifyDTO {
    private String name;
    private String brand;
    private String line;
    private String description;
    private Integer releaseYear;
    private String imageUrl;
    private String gender;
    private String sillage;
    private String confidence;
    private PerfumeNotes notes;
    private List<String> accords;
    private SeasonProfile season;
    private OccasionProfile occasion;
}
