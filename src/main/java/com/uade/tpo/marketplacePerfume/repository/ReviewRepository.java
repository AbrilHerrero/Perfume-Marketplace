package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findBySample_Id(Long sampleId);

    List<Review> findByBuyer_Id(Long buyerId);

    Optional<Review> findByIdAndBuyer_Id(Long id, Long buyerId);

    boolean existsBySample_IdAndBuyer_Id(Long sampleId, Long buyerId);
}
