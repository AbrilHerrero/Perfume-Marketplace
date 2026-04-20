package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Sample;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {

    List<Sample> findByActiveTrue();

    List<Sample> findBySeller_IdAndActiveTrue(Long sellerId);

    List<Sample> findByPerfume_IdAndActiveTrue(Long perfumeId);

    @Modifying
    @Query("UPDATE Sample s SET s.stock = s.stock - :qty WHERE s.id = :id AND s.stock >= :qty")
    int decrementStock(@Param("id") Long id, @Param("qty") int qty);

    @Modifying
    @Query("UPDATE Sample s SET s.stock = s.stock + :qty WHERE s.id = :id")
    int incrementStock(@Param("id") Long id, @Param("qty") int qty);
}