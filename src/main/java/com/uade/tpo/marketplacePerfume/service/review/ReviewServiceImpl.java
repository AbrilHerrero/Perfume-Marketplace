package com.uade.tpo.marketplacePerfume.service.review;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Review;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewAlreadyExistsException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewIncompleteRequestException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewInvalidRatingException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewNotOwnedException;
import com.uade.tpo.marketplacePerfume.mapper.ReviewMapper;
import com.uade.tpo.marketplacePerfume.repository.ReviewRepository;
import com.uade.tpo.marketplacePerfume.service.sample.ISampleService;

@Service
public class ReviewServiceImpl implements IReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ISampleService sampleService;

    @Override
    public List<ReviewResponseDTO> getReviewsBySampleId(Long sampleId) {
        sampleService.getSampleById(sampleId); // valida que el sample exista y esté activo
        return ReviewMapper.toResponseDtoList(reviewRepository.findBySample_Id(sampleId));
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByBuyerId(Long buyerId) {
        return ReviewMapper.toResponseDtoList(reviewRepository.findByBuyer_Id(buyerId));
    }

    @Override
    public ReviewResponseDTO getReviewById(Long id) {
        return ReviewMapper.toResponseDto(findByIdOrThrow(id));
    }

    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO dto, User buyer) {
        validateRequest(dto);

        Sample sample = sampleService.getSampleById(dto.getSampleId());

        if (reviewRepository.existsBySample_IdAndBuyer_Id(sample.getId(), buyer.getId())) {
            throw new ReviewAlreadyExistsException();
        }

        Review review = ReviewMapper.toEntityFromRequest(dto);
        review.setBuyer(buyer);
        review.setSample(sample);
        review.setCreatedAt(LocalDateTime.now());

        return ReviewMapper.toResponseDto(reviewRepository.save(review));
    }

    @Override
    public ReviewResponseDTO updateReview(Long id, ReviewRequestDTO dto, User buyer) {
        validateRequest(dto);

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

    private void validateRequest(ReviewRequestDTO dto) {
        if (dto == null
                || dto.getSampleId() == null
                || dto.getRating() == null
                || dto.getComment() == null || dto.getComment().isBlank()) {
            throw new ReviewIncompleteRequestException();
        }
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new ReviewInvalidRatingException();
        }
    }

    @Override
    public List<ReviewResponseDTO> getReviewsBySellerId(Long sellerId) {
        return ReviewMapper.toResponseDtoList(reviewRepository.findBySample_Seller_Id(sellerId));
    }

}