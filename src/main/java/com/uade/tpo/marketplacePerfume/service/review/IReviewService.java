package com.uade.tpo.marketplacePerfume.service.review;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewListResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewReplyRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewUpdateRequestDTO;

public interface IReviewService {
    ReviewResponseDTO getReviewById(Long id);
    ReviewListResponseDTO getReviewsBySampleId(Long sampleId);
    ReviewListResponseDTO getReviewsByBuyerId(Long buyerId);
    ReviewListResponseDTO getReviewsBySellerId(Long sellerId);
    ReviewResponseDTO createReview(ReviewRequestDTO dto, User buyer);
    ReviewResponseDTO updateReview(Long id, ReviewUpdateRequestDTO dto, User buyer);
    ReviewResponseDTO replyToReview(Long id, ReviewReplyRequestDTO dto, User seller);
    void deleteReview(Long id, User buyer);
}
