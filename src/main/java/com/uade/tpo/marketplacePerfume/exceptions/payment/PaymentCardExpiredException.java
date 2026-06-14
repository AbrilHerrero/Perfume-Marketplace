package com.uade.tpo.marketplacePerfume.exceptions.payment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The card has expired or the expiry date is invalid")
public class PaymentCardExpiredException extends RuntimeException {
}
