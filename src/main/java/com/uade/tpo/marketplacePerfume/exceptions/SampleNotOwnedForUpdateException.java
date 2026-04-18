package com.uade.tpo.marketplacePerfume.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "You can only update your own samples")
public class SampleNotOwnedForUpdateException extends RuntimeException {
}
