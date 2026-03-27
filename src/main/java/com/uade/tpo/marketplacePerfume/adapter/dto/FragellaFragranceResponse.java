package com.uade.tpo.marketplacePerfume.adapter.dto;

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

    @JsonProperty("Image URL")
    private String imageUrl;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("Sillage")
    private String sillage;

    @JsonProperty("Confidence")
    private String confidence;
}
