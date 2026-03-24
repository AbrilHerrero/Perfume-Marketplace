package com.uade.tpo.marketplacePerfume.adapter.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FragellaNotesResponse {

    @JsonProperty("Top")
    private List<FragellaNoteItem> top;

    @JsonProperty("Middle")
    private List<FragellaNoteItem> middle;

    @JsonProperty("Base")
    private List<FragellaNoteItem> base;
}
