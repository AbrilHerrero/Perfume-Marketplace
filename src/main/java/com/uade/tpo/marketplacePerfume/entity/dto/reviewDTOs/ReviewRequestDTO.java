package com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs;

import lombok.Data;

@Data
public class ReviewRequestDTO {
    private Long sampleId;
    private Integer rating;
    private String comment;
}