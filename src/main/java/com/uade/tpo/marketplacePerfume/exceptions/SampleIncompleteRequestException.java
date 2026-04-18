package com.uade.tpo.marketplacePerfume.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SampleIncompleteRequestException extends RuntimeException {

    public SampleIncompleteRequestException() {
        super("All sample fields must be provided (price, stock, volumeMl, description, imageUrl, perfumeId).");
    }
}
