package com.uade.tpo.marketplacePerfume.exceptions.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Order does not exist")
public class OrderNotFoundException extends RuntimeException {
}
