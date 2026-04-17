package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.marketplacePerfume.entity.Sample;

public interface SampleRepository extends JpaRepository<Sample, Long> {
    List<Sample> findBySellerIdAndActiveTrue(Long sellerId);
}