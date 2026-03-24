package com.uade.tpo.marketplacePerfume.adapter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FragellaNoteItem {

    private String name;
    private String imageUrl;
}
