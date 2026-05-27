package com.uade.tpo.marketplacePerfume.exceptions.coupon;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "A coupon with this code already exists")
public class CouponCodeAlreadyExistsException extends RuntimeException {
}
