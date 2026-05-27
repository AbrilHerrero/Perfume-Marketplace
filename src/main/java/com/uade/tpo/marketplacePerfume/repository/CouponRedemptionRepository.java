package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.CouponRedemption;

@Repository
public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long> {

    boolean existsByCoupon_IdAndBuyer_Id(Long couponId, Long buyerId);

    List<CouponRedemption> findByCoupon_Seller_Id(Long sellerId);

    List<CouponRedemption> findByCoupon_Id(Long couponId);
}
