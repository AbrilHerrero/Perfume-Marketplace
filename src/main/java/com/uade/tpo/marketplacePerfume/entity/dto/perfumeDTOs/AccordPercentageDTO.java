package com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccordPercentageDTO {
    private String accordName;
    private String strength;
}
