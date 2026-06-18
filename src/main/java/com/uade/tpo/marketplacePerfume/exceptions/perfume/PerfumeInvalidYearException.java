package com.uade.tpo.marketplacePerfume.exceptions.perfume;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Release year cannot be negative")
public class PerfumeInvalidYearException extends RuntimeException {
}
