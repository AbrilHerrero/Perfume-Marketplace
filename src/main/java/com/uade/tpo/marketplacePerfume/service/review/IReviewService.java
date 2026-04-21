package com.uade.tpo.marketplacePerfume.service.review;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewResponseDTO;

public interface IReviewService {
    List<ReviewResponseDTO> getReviewsBySampleId(Long sampleId);
    List<ReviewResponseDTO> getReviewsByBuyerId(Long buyerId);
    ReviewResponseDTO getReviewById(Long id);
    ReviewResponseDTO createReview(ReviewRequestDTO dto, User buyer);
    ReviewResponseDTO updateReview(Long id, ReviewRequestDTO dto, User buyer);
    void deleteReview(Long id, User buyer);
}