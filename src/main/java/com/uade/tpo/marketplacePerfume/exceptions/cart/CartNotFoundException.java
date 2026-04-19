package com.uade.tpo.marketplacePerfume.exceptions.cart;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Cart not found")
public class CartNotFoundException extends RuntimeException {
}
