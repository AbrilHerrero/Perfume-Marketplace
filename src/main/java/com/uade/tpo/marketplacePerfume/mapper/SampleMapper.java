package com.uade.tpo.marketplacePerfume.mapper;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleResponseDTO;

public final class SampleMapper {

    private SampleMapper() {}


    public static SampleResponseDTO toResponseDto(Sample entity) {
        if (entity == null) return null;
        SampleResponseDTO dto = new SampleResponseDTO();
        dto.setId(entity.getId());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setVolumeMl(entity.getVolumeMl());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        dto.setRating(entity.getRating());
        dto.setReviewCount(entity.getReviewCount());
        
        if (entity.getSeller() != null) {
            dto.setSellerId(entity.getSeller().getId());
            dto.setSellerName(formatSellerName(entity.getSeller()));
        }
        if (entity.getPerfume() != null) dto.setPerfumeId(entity.getPerfume().getId());
        return dto;
    }

    private static String formatSellerName(com.uade.tpo.marketplacePerfume.entity.User seller) {
        if (seller == null) return null;
        String name = seller.getName() != null ? seller.getName().trim() : "";
        String surname = seller.getSurname() != null ? seller.getSurname().trim() : "";
        if (name.isEmpty() && surname.isEmpty()) return null;
        if (surname.isEmpty()) return name;
        if (name.isEmpty()) return surname;
        return name + " " + surname;
    }

    public static Sample toEntityFromRequest(SampleRequestDTO dto) {
        if (dto == null) return null;
        return Sample.builder()
                .price(dto.getPrice())
                .stock(dto.getStock())
                .volumeMl(dto.getVolumeMl())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .active(true)
                .build();
    }

    public static void applyFullUpdate(SampleRequestDTO dto, Sample existing) {
        if (dto == null) return;
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());
        existing.setVolumeMl(dto.getVolumeMl());
        existing.setDescription(dto.getDescription());
        existing.setImageUrl(dto.getImageUrl());
    }

    public static List<SampleResponseDTO> toResponseDtoList(List<Sample> entities) {
        List<SampleResponseDTO> dtos = new ArrayList<>();
        if (entities != null) {
            for (Sample entity : entities) {
                dtos.add(toResponseDto(entity));
            }
        }
        return dtos;
    }
}