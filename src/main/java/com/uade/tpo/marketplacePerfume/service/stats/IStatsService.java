package com.uade.tpo.marketplacePerfume.service.stats;

import com.uade.tpo.marketplacePerfume.entity.dto.stats.AdminStatsResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.stats.PublicStatsResponseDTO;

public interface IStatsService {
    AdminStatsResponseDTO getAdminStats();
    PublicStatsResponseDTO getPublicStats();
}
