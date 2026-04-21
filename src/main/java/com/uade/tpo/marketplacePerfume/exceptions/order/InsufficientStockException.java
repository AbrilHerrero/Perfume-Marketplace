package com.uade.tpo.marketplacePerfume.exceptions.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Insufficient stock to complete the order")
public class InsufficientStockException extends RuntimeException {
}
