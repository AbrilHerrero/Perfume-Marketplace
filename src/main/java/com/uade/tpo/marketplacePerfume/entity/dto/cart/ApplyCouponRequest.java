package com.uade.tpo.marketplacePerfume.entity.dto.cart;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplyCouponRequest {

    @NotBlank(message = "couponCode is required")
    private String couponCode;
}
