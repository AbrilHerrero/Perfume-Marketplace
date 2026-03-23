package com.uade.tpo.marketplacePerfume.mapper;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.dto.PerfumeDTO;

public final class PerfumeMapper {

    private PerfumeMapper() {
    }

    public static PerfumeDTO toDto(Perfume entity) {
        if (entity == null) {
            return null;
        }

        PerfumeDTO dto = new PerfumeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBrand(entity.getBrand());
        dto.setLine(entity.getLine());
        dto.setDescription(entity.getDescription());
        dto.setReleaseYear(entity.getReleaseYear());
        return dto;
    }

    public static Perfume toEntity(PerfumeDTO dto) {
        if (dto == null) {
            return null;
        }

        return Perfume.builder()
                .id(dto.getId())
                .name(dto.getName())
                .brand(dto.getBrand())
                .line(dto.getLine())
                .description(dto.getDescription())
                .releaseYear(dto.getReleaseYear())
                .build();
    }

    public static List<PerfumeDTO> toDtoList(List<Perfume> entities) {
        List<PerfumeDTO> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }

        for (Perfume entity : entities) {
            dtos.add(toDto(entity));
        }
        return dtos;
    }
}
