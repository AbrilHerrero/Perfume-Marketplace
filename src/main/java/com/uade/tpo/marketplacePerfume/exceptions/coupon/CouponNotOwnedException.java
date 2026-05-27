package com.uade.tpo.marketplacePerfume.exceptions.coupon;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "You can only manage your own coupons")
public class CouponNotOwnedException extends RuntimeException {
}
