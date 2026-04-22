package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrder_Buyer_IdOrderByCreatedAtDesc(Long buyerId);

    Optional<Payment> findByOrder_Id(Long orderId);

    @Query("SELECT DISTINCT p FROM Payment p JOIN FETCH p.order o "
            + "LEFT JOIN FETCH o.orderItems i LEFT JOIN FETCH i.sample s LEFT JOIN FETCH s.seller "
            + "WHERE p.id = :id")
    Optional<Payment> findByIdWithOrderItemsAndSamples(@Param("id") Long id);
}
