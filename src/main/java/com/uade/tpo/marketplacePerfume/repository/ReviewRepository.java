package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findBySample_Id(Long sampleId);

    boolean existsBySample_IdAndBuyer_Id(Long sampleId, Long buyerId);
}
