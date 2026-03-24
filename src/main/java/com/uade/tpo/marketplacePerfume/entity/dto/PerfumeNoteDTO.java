package com.uade.tpo.marketplacePerfume.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PerfumeNoteDTO {
    private String name;
    private String imageUrl;
    private String layer;
}
