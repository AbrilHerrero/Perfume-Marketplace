package com.uade.tpo.marketplacePerfume.mapper;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Review;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewListResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewUpdateRequestDTO;

public final class ReviewMapper {

    private ReviewMapper() {}

    public static ReviewResponseDTO toResponseDto(Review entity) {
        if (entity == null) return null;
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(entity.getId());
        dto.setRating(entity.getRating());
        dto.setComment(entity.getComment());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setSellerReply(entity.getSellerReply());
        dto.setSellerReplyAt(entity.getSellerReplyAt());
        if (entity.getBuyer() != null) {
            dto.setBuyerId(entity.getBuyer().getId());
            dto.setBuyerName(formatBuyerName(entity.getBuyer()));
        }
        if (entity.getSample() != null) {
            dto.setSampleId(entity.getSample().getId());
            if (entity.getSellerReply() != null && entity.getSample().getSeller() != null) {
                dto.setSellerReplyName(entity.getSample().getSeller().getName());
            }
        }
        return dto;
    }

    public static Review toEntityFromRequest(ReviewRequestDTO dto) {
        if (dto == null) return null;
        return Review.builder()
                .rating(dto.getRating())
                .comment(normalizeComment(dto.getComment()))
                .build();
    }

    public static void applyFullUpdate(ReviewUpdateRequestDTO dto, Review existing) {
        if (dto == null) return;
        existing.setRating(dto.getRating());
        existing.setComment(normalizeComment(dto.getComment()));
    }

    private static String formatBuyerName(User buyer) {
        if (buyer == null) return null;
        String name = buyer.getName() != null ? buyer.getName().trim() : "";
        String surname = buyer.getSurname() != null ? buyer.getSurname().trim() : "";
        if (name.isEmpty() && surname.isEmpty()) return null;
        if (surname.isEmpty()) return name;
        if (name.isEmpty()) return surname;
        return name + " " + surname;
    }

    private static String normalizeComment(String comment) {
        if (comment == null) return null;
        String trimmed = comment.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static ReviewListResponseDTO toListResponseDto(List<Review> entities) {
        List<ReviewResponseDTO> dtos = new ArrayList<>();
        long ratingSum = 0L;
        int ratedCount = 0;

        if (entities != null) {
            for (Review entity : entities) {
                dtos.add(toResponseDto(entity));
                if (entity != null && entity.getRating() != null) {
                    ratingSum += entity.getRating();
                    ratedCount++;
                }
            }
        }

        Double averageRating = ratedCount == 0 ? null : (double) ratingSum / ratedCount;

        return ReviewListResponseDTO.builder()
                .reviews(dtos)
                .totalCount((long) dtos.size())
                .averageRating(averageRating)
                .build();
    }
}
