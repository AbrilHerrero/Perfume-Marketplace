package com.uade.tpo.marketplacePerfume.entity.dto.cart;

import java.math.BigDecimal;

import com.uade.tpo.marketplacePerfume.entity.DiscountType;

import lombok.Data;

@Data
public class ApplyCouponResponse {

    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private Long sellerId;
    private BigDecimal cartSubtotal;
    private BigDecimal sellerSubtotal;
    private BigDecimal discountAmount;
    private BigDecimal totalAfterDiscount;
}
