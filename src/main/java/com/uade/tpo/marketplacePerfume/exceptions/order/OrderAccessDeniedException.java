package com.uade.tpo.marketplacePerfume.exceptions.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "You are not authorized to access this order")
public class OrderAccessDeniedException extends RuntimeException {
}
