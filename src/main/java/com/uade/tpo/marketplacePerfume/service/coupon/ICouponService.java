package com.uade.tpo.marketplacePerfume.service.coupon;

import java.math.BigDecimal;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Coupon;
import com.uade.tpo.marketplacePerfume.entity.CouponRedemption;
import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponRedemptionResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponUpdateDTO;

public interface ICouponService {

    // Seller CRUD
    CouponResponseDTO createCoupon(CouponCreateDTO dto, User seller);

    List<CouponResponseDTO> getMyCoupons(User seller);

    CouponResponseDTO getCouponById(Long id, User principal);

    CouponResponseDTO updateCoupon(Long id, CouponUpdateDTO dto, User principal);

    void deleteCoupon(Long id, User principal);

    // Redemption history
    List<CouponRedemptionResponseDTO> getMyCouponHistory(User seller);

    List<CouponRedemptionResponseDTO> getCouponHistory(Long couponId, User principal);

    // Used by the checkout flow
    Coupon validateForRedemption(String code, User buyer);

    BigDecimal computeDiscount(Coupon coupon, BigDecimal sellerSubtotal);

    CouponRedemption recordRedemption(Coupon coupon, User buyer, Order order, BigDecimal discount);

    // Seller dashboard stats
    long countActiveCoupons(User seller);
}
