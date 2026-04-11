package com.uade.tpo.marketplacePerfume.mapper;

import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs.SampleResponseDTO;

public final class SampleMapper {

    private SampleMapper() {
    }

    public static SampleResponseDTO toResponseDto(Sample entity) {
        if (entity == null) {
            return null;
        }
        SampleResponseDTO dto = new SampleResponseDTO();
        dto.setId(entity.getId());
        dto.setVolumeMl(entity.getVolumeMl());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStock(entity.getStock());
        dto.setPerfume(PerfumeMapper.toResponseDto(entity.getPerfume()));
        dto.setSeller(UserMapper.toResponseDto(entity.getSeller()));
        return dto;
    }
}
