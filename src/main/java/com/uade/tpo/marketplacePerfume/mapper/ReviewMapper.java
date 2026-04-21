package com.uade.tpo.marketplacePerfume.mapper;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Review;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewResponseDTO;

public final class ReviewMapper {

    private ReviewMapper() {}

    public static ReviewResponseDTO toResponseDto(Review entity) {
        if (entity == null) return null;
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(entity.getId());
        dto.setRating(entity.getRating());
        dto.setComment(entity.getComment());
        dto.setCreatedAt(entity.getCreatedAt());
        if (entity.getBuyer() != null) dto.setBuyerId(entity.getBuyer().getId());
        if (entity.getSample() != null) dto.setSampleId(entity.getSample().getId());
        return dto;
    }

    public static Review toEntityFromRequest(ReviewRequestDTO dto) {
        if (dto == null) return null;
        return Review.builder()
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
    }

    public static void applyFullUpdate(ReviewRequestDTO dto, Review existing) {
        if (dto == null) return;
        existing.setRating(dto.getRating());
        existing.setComment(dto.getComment());
    }

    public static List<ReviewResponseDTO> toResponseDtoList(List<Review> entities) {
        List<ReviewResponseDTO> dtos = new ArrayList<>();
        if (entities != null) {
            for (Review entity : entities) {
                dtos.add(toResponseDto(entity));
            }
        }
        return dtos;
    }
}