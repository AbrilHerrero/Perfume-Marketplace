package com.uade.tpo.marketplacePerfume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCart_IdAndSample_Id(Long cartId, Long sampleId);

    void deleteAllByCart_Id(Long cartId);
}
