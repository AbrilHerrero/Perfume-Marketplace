package com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs;

import java.util.List;


import lombok.Data;

@Data
public class PerfumeModifyDTO {
    private String name;
    private String brand;
    private String line;
    private String description;
    private Integer releaseYear;
    private String rating;
    private String country;
    private String price;
    private String imageUrl;
    private String gender;
    private String longevity;
    private String sillage;
    private String popularity;
    private String priceValue;
    private String confidence;
    private String oilType;
    private String purchaseUrl;
    private List<String> generalNotes;
    private List<String> mainAccords;
    private List<String> imageFallbacks;
    private List<AccordPercentageDTO> accordPercentages;
    private List<PerfumeNoteDTO> notes;
    private List<RankingDTO> seasonRankings;
    private List<RankingDTO> occasionRankings;
}
