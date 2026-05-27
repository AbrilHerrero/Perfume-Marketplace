package com.uade.tpo.marketplacePerfume.exceptions.coupon;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid coupon configuration")
public class CouponInvalidException extends RuntimeException {
}
