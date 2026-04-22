package com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs;

import lombok.Data;

@Data
public class ReviewUpdateRequestDTO {
    private Integer rating;
    private String comment;
}
