package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.SavedPaymentMethod;

@Repository
public interface SavedPaymentMethodRepository extends JpaRepository<SavedPaymentMethod, Long> {

    List<SavedPaymentMethod> findAllByBuyer_IdAndActiveTrueOrderByIdAsc(Long buyerId);

    Optional<SavedPaymentMethod> findByIdAndBuyer_IdAndActiveTrue(Long id, Long buyerId);
}
