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

    @Query("SELECT COALESCE(SUM(o.discountAmount), 0) FROM Order o "
            + "WHERE o.status IN :statuses "
            + "AND o.createdAt >= :since "
            + "AND o.discountAmount IS NOT NULL "
            + "AND EXISTS (SELECT 1 FROM OrderItem oi WHERE oi.order = o "
            + "AND oi.sample.seller.id = :sellerId) "
            + "AND NOT EXISTS (SELECT 1 FROM OrderItem oi2 WHERE oi2.order = o "
            + "AND oi2.sample.seller.id <> :sellerId)")
    BigDecimal sumDiscountForFullyOwnedSellerOrders(@Param("sellerId") Long sellerId,
            @Param("statuses") Collection<OrderStatus> statuses,
            @Param("since") LocalDateTime since);

    boolean existsByOrder_Buyer_IdAndSample_IdAndOrder_StatusNot(
            Long buyerId, Long sampleId, OrderStatus status);

    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi "
            + "WHERE oi.order.status IN :statuses")
    long sumQuantityByOrderStatuses(@Param("statuses") Collection<OrderStatus> statuses);
}
