package com.uade.tpo.marketplacePerfume.entity.dto.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.uade.tpo.marketplacePerfume.entity.DiscountType;

import lombok.Data;

@Data
public class CouponResponseDTO {

    private Long id;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private boolean active;
    private Long sellerId;
    private LocalDateTime createdAt;
}
