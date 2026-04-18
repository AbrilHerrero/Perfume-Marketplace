package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Sample;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {

    List<Sample> findByActiveTrue();

    List<Sample> findBySeller_IdAndActiveTrue(Long sellerId);
}