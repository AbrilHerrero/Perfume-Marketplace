package com.uade.tpo.marketplacePerfume.entity.dto.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CouponRedemptionResponseDTO {

    private Long id;
    private Long couponId;
    private String couponCode;
    private Long buyerId;
    private String buyerEmail;
    private Long orderId;
    private BigDecimal discountAmount;
    private LocalDateTime redeemedAt;
}
