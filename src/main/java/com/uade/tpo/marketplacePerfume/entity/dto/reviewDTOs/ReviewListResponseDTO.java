package com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponseDTO {
    private List<ReviewResponseDTO> reviews;
    private Long totalCount;
    private Double averageRating;
}
