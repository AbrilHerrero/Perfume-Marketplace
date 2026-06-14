package com.uade.tpo.marketplacePerfume.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.OrderItem;
import com.uade.tpo.marketplacePerfume.entity.OrderStatus;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi "
            + "WHERE oi.sample.seller.id = :sellerId "
            + "AND oi.order.status IN :statuses "
            + "AND oi.order.createdAt >= :since")
    long sumQuantityBySellerSince(@Param("sellerId") Long sellerId,
            @Param("statuses") Collection<OrderStatus> statuses,
            @Param("since") LocalDateTime since);

    @Query("SELECT COALESCE(SUM(oi.unitPrice * oi.quantity), 0) FROM OrderItem oi "
            + "WHERE oi.sample.seller.id = :sellerId "
            + "AND oi.order.status IN :statuses "
            + "AND oi.order.createdAt >= :since")
    BigDecimal sumRevenueBySellerSince(@Param("sellerId") Long sellerId,
            @Param("statuses") Collection<OrderStatus> statuses,
            @Param("since") LocalDateTime since);
}
