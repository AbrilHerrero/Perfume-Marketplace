package com.uade.tpo.marketplacePerfume.service.review;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Review;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewListResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewUpdateRequestDTO;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewAlreadyExistsException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewCommentTooLongException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewIncompleteRequestException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewInvalidRatingException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewNotOwnedException;
import com.uade.tpo.marketplacePerfume.mapper.ReviewMapper;
import com.uade.tpo.marketplacePerfume.repository.ReviewRepository;
import com.uade.tpo.marketplacePerfume.service.sample.ISampleService;

@Service
public class ReviewServiceImpl implements IReviewService {

    private static final int MAX_COMMENT_LENGTH = 1000;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ISampleService sampleService;

    @Override
    public ReviewListResponseDTO getReviewsBySampleId(Long sampleId) {
        sampleService.getSampleById(sampleId); // valida que el sample exista y esté activo
        return ReviewMapper.toListResponseDto(reviewRepository.findBySample_Id(sampleId));
    }

    @Override
    public ReviewListResponseDTO getReviewsByBuyerId(Long buyerId) {
        return ReviewMapper.toListResponseDto(reviewRepository.findByBuyer_Id(buyerId));
    }

    @Override
    public ReviewListResponseDTO getReviewsBySellerId(Long sellerId) {
        return ReviewMapper.toListResponseDto(reviewRepository.findBySample_Seller_Id(sellerId));
    }

    @Override
    public ReviewResponseDTO getReviewById(Long id) {
        return ReviewMapper.toResponseDto(findByIdOrThrow(id));
    }

    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO dto, User buyer) {
        validateCreateRequest(dto);

        Sample sample = sampleService.getSampleById(dto.getSampleId());

        if (reviewRepository.existsBySample_IdAndBuyer_Id(sample.getId(), buyer.getId())) {
            throw new ReviewAlreadyExistsException();
        }

        Review review = ReviewMapper.toEntityFromRequest(dto);
        review.setBuyer(buyer);
        review.setSample(sample);
        review.setCreatedAt(LocalDateTime.now());

        try {
            return ReviewMapper.toResponseDto(reviewRepository.save(review));
        } catch (DataIntegrityViolationException e) {
            throw new ReviewAlreadyExistsException();
        }
    }

    @Override
    public ReviewResponseDTO updateReview(Long id, ReviewUpdateRequestDTO dto, User buyer) {
        validateUpdateRequest(dto);

        Review existing = findByIdOrThrow(id);

        if (!existing.getBuyer().getId().equals(buyer.getId())) {
            throw new ReviewNotOwnedException();
        }

        ReviewMapper.applyFullUpdate(dto, existing);

        return ReviewMapper.toResponseDto(reviewRepository.save(existing));
    }

    @Override
    public void deleteReview(Long id, User buyer) {
        Review review = findByIdOrThrow(id);

        if (!review.getBuyer().getId().equals(buyer.getId())) {
            throw new ReviewNotOwnedException();
        }

        reviewRepository.delete(review);
    }

    private Review findByIdOrThrow(Long id) {
        return reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
    }

    private void validateCreateRequest(ReviewRequestDTO dto) {
        if (dto == null || dto.getSampleId() == null) {
            throw new ReviewIncompleteRequestException();
        }
        validateRating(dto.getRating());
        validateCommentLength(dto.getComment());
    }

    private void validateUpdateRequest(ReviewUpdateRequestDTO dto) {
        if (dto == null) {
            throw new ReviewIncompleteRequestException();
        }
        validateRating(dto.getRating());
        validateCommentLength(dto.getComment());
    }

    private void validateRating(Integer rating) {
        if (rating == null) {
            throw new ReviewIncompleteRequestException();
        }
        if (rating < 1 || rating > 5) {
            throw new ReviewInvalidRatingException();
        }
    }

    private void validateCommentLength(String comment) {
        if (comment != null && comment.trim().length() > MAX_COMMENT_LENGTH) {
            throw new ReviewCommentTooLongException();
        }
    }

}