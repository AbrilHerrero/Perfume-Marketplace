package com.uade.tpo.marketplacePerfume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByBuyer_Id(Long buyerId);

    @Query("SELECT DISTINCT c FROM Cart c "
            + "LEFT JOIN FETCH c.cartItems ci "
            + "LEFT JOIN FETCH ci.sample s "
            + "LEFT JOIN FETCH s.seller "
            + "WHERE c.buyer.id = :buyerId")
    Optional<Cart> findByBuyer_IdWithItems(@Param("buyerId") Long buyerId);
}
