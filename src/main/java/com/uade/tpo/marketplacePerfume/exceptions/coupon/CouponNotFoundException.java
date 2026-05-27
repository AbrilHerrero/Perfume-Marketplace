package com.uade.tpo.marketplacePerfume.exceptions.coupon;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Coupon not found")
public class CouponNotFoundException extends RuntimeException {
}
