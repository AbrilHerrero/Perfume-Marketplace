package com.uade.tpo.marketplacePerfume.mapper;

import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartSampleSummaryDTO;

public final class SampleMapper {

    private SampleMapper() {
    }

    /** Cart / cart-item payloads: product + minimal seller, no internal user fields. */
    public static CartSampleSummaryDTO toCartSampleSummaryDto(Sample entity) {
        if (entity == null) {
            return null;
        }
        CartSampleSummaryDTO dto = new CartSampleSummaryDTO();
        dto.setId(entity.getId());
        dto.setVolumeMl(entity.getVolumeMl());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        dto.setStock(entity.getStock());
        dto.setPerfume(PerfumeMapper.toResponseDto(entity.getPerfume()));
        dto.setSeller(UserMapper.toSellerSummary(entity.getSeller()));
        return dto;
    }
}
