package com.uade.tpo.marketplacePerfume.exceptions.cartItem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Insufficient stock for the requested sample")
public class CartItemInsufficientStockException extends RuntimeException {
}
