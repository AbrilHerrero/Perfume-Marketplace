package com.uade.tpo.marketplacePerfume.service.review;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewListResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewReplyRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewResponseDTO;

public interface IReviewService {
    ReviewListResponseDTO getReviewsBySampleId(Long sampleId);
    ReviewResponseDTO createReview(ReviewRequestDTO dto, User buyer);
    ReviewResponseDTO replyToReview(Long id, ReviewReplyRequestDTO dto, User seller);
}
