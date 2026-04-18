package com.uade.tpo.marketplacePerfume.exceptions.sample;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Stock must be zero or positive")
public class StockUpdateInvalidException extends RuntimeException {
}
