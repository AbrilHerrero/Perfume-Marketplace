package com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private Long id;
    private Long buyerId;
    private Long sampleId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}