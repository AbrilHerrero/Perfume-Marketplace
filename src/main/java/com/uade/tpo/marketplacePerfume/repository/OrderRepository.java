package com.uade.tpo.marketplacePerfume.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyerId(Long buyerId);

    long countByCreatedAtGreaterThanEqualAndStatusIn(LocalDateTime since, Collection<OrderStatus> statuses);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o "
            + "WHERE o.createdAt >= :since AND o.status <> :excludedStatus")
    BigDecimal sumTotalSinceExcludingStatus(@Param("since") LocalDateTime since,
            @Param("excludedStatus") OrderStatus excludedStatus);

    @Query("SELECT DISTINCT o FROM Order o "
            + "JOIN o.orderItems oi "
            + "WHERE oi.sample.seller.id = :sellerId "
            + "ORDER BY o.createdAt DESC")
    List<Order> findBySellerId(@Param("sellerId") Long sellerId);

    @Query("SELECT DISTINCT o FROM Order o "
            + "LEFT JOIN FETCH o.buyer "
            + "LEFT JOIN FETCH o.orderItems oi "
            + "LEFT JOIN FETCH oi.sample s "
            + "LEFT JOIN FETCH s.seller "
            + "WHERE o.id = :id")
    Optional<Order> findByIdWithBuyerAndItems(@Param("id") Long id);

    @Query("SELECT DISTINCT o FROM Order o "
            + "LEFT JOIN FETCH o.buyer "
            + "LEFT JOIN FETCH o.orderItems oi "
            + "LEFT JOIN FETCH oi.sample s "
            + "LEFT JOIN FETCH s.seller "
            + "ORDER BY o.createdAt DESC")
    List<Order> findAllWithBuyerAndItems();
}
