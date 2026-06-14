package com.uade.tpo.marketplacePerfume.exceptions.payment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Saved payment method not found")
public class SavedPaymentMethodNotFoundException extends RuntimeException {
}
