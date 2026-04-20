package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyerId(Long buyerId);

    @Query("SELECT DISTINCT o FROM Order o "
            + "LEFT JOIN FETCH o.buyer "
            + "LEFT JOIN FETCH o.orderItems oi "
            + "LEFT JOIN FETCH oi.sample s "
            + "LEFT JOIN FETCH s.seller "
            + "WHERE o.id = :id")
    Optional<Order> findByIdWithBuyerAndItems(@Param("id") Long id);
}
