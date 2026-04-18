package com.uade.tpo.marketplacePerfume.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SampleNotFoundException extends RuntimeException {
    public SampleNotFoundException(Long id) {
        super("Sample not found with ID: " + id);
    }
}