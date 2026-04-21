package com.uade.tpo.marketplacePerfume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    boolean existsByOrder_Id(Long orderId);

    @Query("SELECT DISTINCT s FROM Shipment s "
            + "JOIN FETCH s.order o "
            + "JOIN FETCH o.buyer "
            + "LEFT JOIN FETCH o.orderItems oi "
            + "LEFT JOIN FETCH oi.sample samp "
            + "LEFT JOIN FETCH samp.seller "
            + "LEFT JOIN FETCH s.address addr "
            + "WHERE s.id = :id")
    Optional<Shipment> findFetchedById(@Param("id") Long id);

    @Query("SELECT DISTINCT s FROM Shipment s "
            + "JOIN FETCH s.order o "
            + "JOIN FETCH o.buyer "
            + "LEFT JOIN FETCH o.orderItems oi "
            + "LEFT JOIN FETCH oi.sample samp "
            + "LEFT JOIN FETCH samp.seller "
            + "LEFT JOIN FETCH s.address addr "
            + "WHERE o.id = :orderId")
    Optional<Shipment> findFetchedByOrderId(@Param("orderId") Long orderId);
}
