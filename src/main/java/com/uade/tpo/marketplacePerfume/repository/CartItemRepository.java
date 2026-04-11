package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCart_IdAndSample_Id(Long cartId, Long sampleId);

    void deleteByCart_Id(Long cartId);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.sample s JOIN FETCH s.perfume LEFT JOIN FETCH s.seller WHERE ci.cart.id = :cartId")
    List<CartItem> findWithDetailsByCart_Id(@Param("cartId") Long cartId);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.cart c JOIN FETCH c.buyer JOIN FETCH ci.sample s JOIN FETCH s.perfume LEFT JOIN FETCH s.seller WHERE ci.id = :id")
    Optional<CartItem> findWithDetailsById(@Param("id") Long id);
}
