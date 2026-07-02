package com.uade.tpo.marketplacePerfume.service.review;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.OrderStatus;
import com.uade.tpo.marketplacePerfume.entity.Review;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewListResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewReplyRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewAlreadyExistsException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewCommentTooLongException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewIncompleteRequestException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewInvalidRatingException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewReplyNotOwnedException;
import com.uade.tpo.marketplacePerfume.exceptions.review.ReviewPurchaseRequiredException;
import com.uade.tpo.marketplacePerfume.exceptions.sample.SampleNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.ReviewMapper;
import com.uade.tpo.marketplacePerfume.repository.OrderItemRepository;
import com.uade.tpo.marketplacePerfume.repository.ReviewRepository;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;
import com.uade.tpo.marketplacePerfume.service.sample.ISampleService;

@Service
public class ReviewServiceImpl implements IReviewService {

    private static final int MAX_COMMENT_LENGTH = 1000;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ISampleService sampleService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @Override
    public ReviewListResponseDTO getReviewsBySampleId(Long sampleId) {
        sampleService.getSampleById(sampleId); // valida que el sample exista y esté activo
        return ReviewMapper.toListResponseDto(reviewRepository.findBySample_Id(sampleId));
    }

    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO dto, User buyer) {
        validateCreateRequest(dto);

        Sample sample = sampleService.getSampleById(dto.getSampleId());

        assertBuyerPurchasedSample(buyer.getId(), sample.getId());

        if (reviewRepository.existsBySample_IdAndBuyer_Id(sample.getId(), buyer.getId())) {
            throw new ReviewAlreadyExistsException();
        }

        Review review = ReviewMapper.toEntityFromRequest(dto);
        review.setBuyer(buyer);
        review.setSample(sample);
        LocalDateTime now = LocalDateTime.now();
        review.setCreatedAt(now);
        review.setUpdatedAt(now);

        try {
            Review saved = reviewRepository.save(review);
            recalculateSampleRating(sample.getId());
            return ReviewMapper.toResponseDto(saved);
        } catch (DataIntegrityViolationException e) {
            throw new ReviewAlreadyExistsException();
        }
    }

    @Override
    public ReviewResponseDTO replyToReview(Long id, ReviewReplyRequestDTO dto, User seller) {
        Review review = findByIdOrThrow(id);

        Sample sample = review.getSample();
        if (sample == null || sample.getSeller() == null
                || !sample.getSeller().getId().equals(seller.getId())) {
            throw new ReviewReplyNotOwnedException();
        }

        String reply = dto == null ? null : dto.getReply();
        if (reply == null || reply.trim().isEmpty()) {
            throw new ReviewIncompleteRequestException();
        }
        validateCommentLength(reply);

        review.setSellerReply(reply.trim());
        review.setSellerReplyAt(LocalDateTime.now());
        return ReviewMapper.toResponseDto(reviewRepository.save(review));
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

    private void assertBuyerPurchasedSample(Long buyerId, Long sampleId) {
        boolean purchased = orderItemRepository.existsByOrder_Buyer_IdAndSample_IdAndOrder_StatusNot(
                buyerId, sampleId, OrderStatus.CANCELLED);
        if (!purchased) {
            throw new ReviewPurchaseRequiredException();
        }
    }

    /** Sets the sample rating to the average of its reviews and the count to how many it has. */
    private void recalculateSampleRating(Long sampleId) {
        Sample sample = sampleRepository.findById(sampleId).orElseThrow(SampleNotFoundException::new);
        List<Review> reviews = reviewRepository.findBySample_Id(sampleId);

        if (reviews.isEmpty()) {
            sample.setRating(null);
            sample.setReviewCount(0);
        } else {
            double average = reviews.stream().mapToInt(Review::getRating).average().orElse(0);
            sample.setRating(roundToOneDecimal(average));
            sample.setReviewCount(reviews.size());
        }
        sampleRepository.save(sample);
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

}
