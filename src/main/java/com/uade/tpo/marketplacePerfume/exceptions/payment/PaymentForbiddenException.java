package com.uade.tpo.marketplacePerfume.exceptions.payment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "You are not allowed to access this payment")
public class PaymentForbiddenException extends RuntimeException {
}
