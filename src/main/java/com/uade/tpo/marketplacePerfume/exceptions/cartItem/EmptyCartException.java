package com.uade.tpo.marketplacePerfume.exceptions.cartItem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El carrito esta vacio")
public class EmptyCartException extends RuntimeException {
}
