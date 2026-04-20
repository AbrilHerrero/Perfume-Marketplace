package com.uade.tpo.marketplacePerfume.exceptions.cartItem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Cart line quantity must be at least 1")
public class CartItemInvalidQuantityException extends RuntimeException {
}
