package com.uade.tpo.marketplacePerfume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.OrderItem;
import com.uade.tpo.marketplacePerfume.entity.OrderStatus;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    boolean existsByOrder_Buyer_IdAndSample_IdAndOrder_StatusNot(
            Long buyerId, Long sampleId, OrderStatus status);
}
