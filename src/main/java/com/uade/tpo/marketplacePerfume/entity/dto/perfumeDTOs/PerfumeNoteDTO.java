package com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PerfumeNoteDTO {
    private String name;
    private String imageUrl;
    private String layer;
}
