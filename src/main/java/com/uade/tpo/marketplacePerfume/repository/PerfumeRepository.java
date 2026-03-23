package com.uade.tpo.marketplacePerfume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Perfume;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
}
