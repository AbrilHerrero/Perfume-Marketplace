package com.uade.tpo.marketplacePerfume.exceptions.cartItem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The cart is empty")
public class EmptyCartException extends RuntimeException {
}
