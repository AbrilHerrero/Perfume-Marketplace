package com.uade.tpo.marketplacePerfume.entity.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicStatsResponseDTO {
    private long perfumes;
    private long verifiedSellers;
    private long decantsShipped;
}
