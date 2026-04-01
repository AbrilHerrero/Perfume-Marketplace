package com.uade.tpo.marketplacePerfume.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
    // id, reviewerId, sampleId, rating, comment, createdAt
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn (name = "buyerId",referencedColumnName = "id")
    private User buyer;

    @ManyToOne
    @JoinColumn (name = "sampleId", referencedColumnName = "id")
    private Sample sample;

}
