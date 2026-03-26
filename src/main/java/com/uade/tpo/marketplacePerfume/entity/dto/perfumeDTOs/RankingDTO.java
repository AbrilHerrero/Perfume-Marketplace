package com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankingDTO {
    private String name;
    private Double score;
}
