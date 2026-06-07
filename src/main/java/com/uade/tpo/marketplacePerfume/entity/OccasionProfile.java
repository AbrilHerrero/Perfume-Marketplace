package com.uade.tpo.marketplacePerfume.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OccasionProfile {
    @JsonProperty("Daily")
    private double daily;

    @JsonProperty("Office")
    private double office;

    @JsonProperty("Evening")
    private double evening;

    @JsonProperty("Special")
    private double special;
}
