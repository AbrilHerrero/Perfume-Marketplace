package com.uade.tpo.marketplacePerfume.exceptions.perfume;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Perfume not found")
public class PerfumeNotFoundException extends RuntimeException {
}
