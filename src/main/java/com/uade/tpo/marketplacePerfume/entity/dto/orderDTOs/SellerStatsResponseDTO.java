package com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerStatsResponseDTO {
    private long soldLast30Days;
    private BigDecimal revenueLast30Days;
}
