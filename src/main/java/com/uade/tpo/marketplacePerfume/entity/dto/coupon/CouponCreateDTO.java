package com.uade.tpo.marketplacePerfume.entity.dto.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.uade.tpo.marketplacePerfume.entity.DiscountType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CouponCreateDTO {

    @NotBlank
    private String code;

    @NotNull
    private DiscountType discountType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal discountValue;

    @DecimalMin(value = "0.0", message = "minOrder must be zero or positive")
    private BigDecimal minOrder;

    @Min(value = 1, message = "maxUses must be at least 1")
    private Integer maxUses;

    private LocalDateTime validFrom;

    private LocalDateTime validUntil;
}
