package com.uade.tpo.marketplacePerfume.exceptions.cart;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Cart item not found")
public class CartItemNotFoundException extends RuntimeException {
}
