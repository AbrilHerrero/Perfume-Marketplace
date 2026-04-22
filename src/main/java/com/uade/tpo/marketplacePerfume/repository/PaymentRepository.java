package com.uade.tpo.marketplacePerfume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByOrder_Id(Long orderId);

    @Query("SELECT DISTINCT p FROM Payment p "
            + "JOIN FETCH p.order o "
            + "JOIN FETCH o.buyer "
            + "WHERE p.id = :id")
    Optional<Payment> findFetchedById(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Payment p "
            + "JOIN FETCH p.order o "
            + "JOIN FETCH o.buyer "
            + "WHERE o.id = :orderId")
    Optional<Payment> findFetchedByOrderId(@Param("orderId") Long orderId);
}
