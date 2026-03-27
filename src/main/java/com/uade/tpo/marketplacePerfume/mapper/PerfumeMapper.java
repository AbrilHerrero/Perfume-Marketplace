package com.uade.tpo.marketplacePerfume.mapper;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplacePerfume.adapter.dto.FragellaFragranceResponse;
import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeModifyDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeResponseDTO;

public final class PerfumeMapper {

    private PerfumeMapper() {
    }

    public static PerfumeResponseDTO toResponseDto(Perfume entity) {
        if (entity == null) {
            return null;
        }

        PerfumeResponseDTO dto = new PerfumeResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBrand(entity.getBrand());
        dto.setReleaseYear(entity.getReleaseYear());
        dto.setImageUrl(entity.getImageUrl());
        dto.setGender(entity.getGender());
        dto.setSillage(entity.getSillage());
        dto.setConfidence(entity.getConfidence());

        return dto;
    }

    public static Perfume toEntityFromCreate(PerfumeCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        return Perfume.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .releaseYear(dto.getReleaseYear() != null ? dto.getReleaseYear() : 0)
                .imageUrl(dto.getImageUrl())
                .gender(dto.getGender())
                .sillage(dto.getSillage())
                .confidence(dto.getConfidence())
                .build();
    }

    public static void applyModify(PerfumeModifyDTO dto, Perfume existing) {
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getBrand() != null) existing.setBrand(dto.getBrand());
        if (dto.getReleaseYear() != null) existing.setReleaseYear(dto.getReleaseYear());
        if (dto.getImageUrl() != null) existing.setImageUrl(dto.getImageUrl());
        if (dto.getGender() != null) existing.setGender(dto.getGender());
        if (dto.getSillage() != null) existing.setSillage(dto.getSillage());
        if (dto.getConfidence() != null) existing.setConfidence(dto.getConfidence());
    }

    public static List<PerfumeResponseDTO> toResponseDtoList(List<Perfume> entities) {
        List<PerfumeResponseDTO> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }

        for (Perfume entity : entities) {
            dtos.add(toResponseDto(entity));
        }
        return dtos;
    }

    public static Perfume fromApiResponse(FragellaFragranceResponse api) {
        if (api == null) {
            return null;
        }

        return Perfume.builder()
                .name(api.getName())
                .brand(api.getBrand())
                .releaseYear(parseYear(api.getYear()))
                .imageUrl(api.getImageUrl())
                .gender(api.getGender())
                .sillage(api.getSillage())
                .confidence(api.getConfidence())
                .build();
    }

    private static int parseYear(String year) {
        try {
            return year != null && !year.isBlank() ? Integer.parseInt(year.trim()) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
