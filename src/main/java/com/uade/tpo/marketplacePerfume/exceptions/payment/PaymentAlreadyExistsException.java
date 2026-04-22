package com.uade.tpo.marketplacePerfume.exceptions.payment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "This order already has a payment")
public class PaymentAlreadyExistsException extends RuntimeException {
}
