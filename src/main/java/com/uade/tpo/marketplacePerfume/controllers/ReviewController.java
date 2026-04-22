package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewListResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewUpdateRequestDTO;
import com.uade.tpo.marketplacePerfume.service.review.IReviewService;

@RestController
@RequestMapping("review")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/sample/{sampleId}")
    public ResponseEntity<ReviewListResponseDTO> getBySampleId(@PathVariable Long sampleId) {
        return ResponseEntity.ok(reviewService.getReviewsBySampleId(sampleId));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<ReviewListResponseDTO> getByBuyerId(@PathVariable Long buyerId) {
        return ResponseEntity.ok(reviewService.getReviewsByBuyerId(buyerId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ReviewListResponseDTO> getBySellerId(@PathVariable Long sellerId) {
        return ResponseEntity.ok(reviewService.getReviewsBySellerId(sellerId));
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(
            @RequestBody ReviewRequestDTO dto,
            @AuthenticationPrincipal User buyer) {
        ReviewResponseDTO created = reviewService.createReview(dto, buyer);
        return ResponseEntity.created(URI.create("/review/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> update(
            @PathVariable Long id,
            @RequestBody ReviewUpdateRequestDTO dto,
            @AuthenticationPrincipal User buyer) {
        return ResponseEntity.ok(reviewService.updateReview(id, dto, buyer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User buyer) {
        reviewService.deleteReview(id, buyer);
        return ResponseEntity.ok("Review successfully deleted");
    }
}