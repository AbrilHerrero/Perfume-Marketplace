package com.uade.tpo.marketplacePerfume.exceptions.sample;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Sample not found")
public class SampleNotFoundException extends RuntimeException {
}
