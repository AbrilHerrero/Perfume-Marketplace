package com.uade.tpo.marketplacePerfume.adapter.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FragellaFragranceResponse {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Brand")
    private String brand;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("rating")
    private String rating;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Price")
    private String price;

    @JsonProperty("Image URL")
    private String imageUrl;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("Longevity")
    private String longevity;

    @JsonProperty("Sillage")
    private String sillage;

    @JsonProperty("Popularity")
    private String popularity;

    @JsonProperty("Price Value")
    private String priceValue;

    @JsonProperty("Confidence")
    private String confidence;

    @JsonProperty("OilType")
    private String oilType;

    @JsonProperty("Purchase URL")
    private String purchaseUrl;

    @JsonProperty("General Notes")
    private List<String> generalNotes;

    @JsonProperty("Main Accords")
    private List<String> mainAccords;

    @JsonProperty("Main Accords Percentage")
    private Map<String, String> mainAccordsPercentage;

    @JsonProperty("Notes")
    private FragellaNotesResponse notes;

    @JsonProperty("Season Ranking")
    private List<FragellaRankingResponse> seasonRanking;

    @JsonProperty("Occasion Ranking")
    private List<FragellaRankingResponse> occasionRanking;

    @JsonProperty("Image Fallbacks")
    private List<String> imageFallbacks;
}
