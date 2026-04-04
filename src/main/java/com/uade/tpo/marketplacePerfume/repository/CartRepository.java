package com.uade.tpo.marketplacePerfume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
