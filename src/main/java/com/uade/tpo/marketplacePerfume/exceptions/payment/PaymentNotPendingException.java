package com.uade.tpo.marketplacePerfume.exceptions.payment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,
        reason = "The payment can only be modified while it is PENDING")
public class PaymentNotPendingException extends RuntimeException {
}
