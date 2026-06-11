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
public class SeasonProfile {
    @JsonProperty("Spring")
    private double spring;

    @JsonProperty("Summer")
    private double summer;

    @JsonProperty("Autumn")
    private double autumn;

    @JsonProperty("Winter")
    private double winter;
}
