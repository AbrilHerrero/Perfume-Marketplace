package com.uade.tpo.marketplacePerfume.entity.dto.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.uade.tpo.marketplacePerfume.entity.DiscountType;

import lombok.Data;

@Data
public class CouponUpdateDTO {

    private DiscountType discountType;

    private BigDecimal discountValue;

    private LocalDateTime validFrom;

    private LocalDateTime validUntil;

    private Boolean active;
}
