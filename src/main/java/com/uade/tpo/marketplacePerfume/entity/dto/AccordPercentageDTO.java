package com.uade.tpo.marketplacePerfume.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccordPercentageDTO {
    private String accordName;
    private String strength;
}
