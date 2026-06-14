package com.uade.tpo.marketplacePerfume.entity.dto.stats;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminStatsResponseDTO {
    private long users;
    private long sellers;
    private long activeOrdersToday;
    private BigDecimal gmvLast30Days;
}
