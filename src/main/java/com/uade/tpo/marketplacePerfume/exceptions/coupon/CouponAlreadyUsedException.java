package com.uade.tpo.marketplacePerfume.exceptions.coupon;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "You have already used this coupon")
public class CouponAlreadyUsedException extends RuntimeException {
}
