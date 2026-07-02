package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewListResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewReplyRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.reviewDTOs.ReviewResponseDTO;
import com.uade.tpo.marketplacePerfume.service.review.IReviewService;

@RestController
@RequestMapping("review")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @GetMapping("/sample/{sampleId}")
    public ResponseEntity<ReviewListResponseDTO> getBySampleId(@PathVariable Long sampleId) {
        return ResponseEntity.ok(reviewService.getReviewsBySampleId(sampleId));
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(
            @RequestBody ReviewRequestDTO dto,
            @AuthenticationPrincipal User buyer) {
        ReviewResponseDTO created = reviewService.createReview(dto, buyer);
        return ResponseEntity.created(URI.create("/review/" + created.getId())).body(created);
    }

    @PutMapping("/{id}/reply")
    public ResponseEntity<ReviewResponseDTO> reply(
            @PathVariable Long id,
            @RequestBody ReviewReplyRequestDTO dto,
            @AuthenticationPrincipal User seller) {
        return ResponseEntity.ok(reviewService.replyToReview(id, dto, seller));
    }
}
