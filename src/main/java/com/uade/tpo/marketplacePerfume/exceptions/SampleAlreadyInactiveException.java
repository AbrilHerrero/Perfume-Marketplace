package com.uade.tpo.marketplacePerfume.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Sample is already inactive")
public class SampleAlreadyInactiveException extends RuntimeException {
}
