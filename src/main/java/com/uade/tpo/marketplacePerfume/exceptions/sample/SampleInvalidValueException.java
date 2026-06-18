package com.uade.tpo.marketplacePerfume.exceptions.sample;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Price and volume must be greater than 0 and stock cannot be negative")
public class SampleInvalidValueException extends RuntimeException {
}
