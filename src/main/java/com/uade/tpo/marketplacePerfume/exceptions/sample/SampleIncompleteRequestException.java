package com.uade.tpo.marketplacePerfume.exceptions.sample;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.BAD_REQUEST,
        reason = "All sample fields must be provided (price, stock, volumeMl, description, imageUrl, perfumeId).")
public class SampleIncompleteRequestException extends RuntimeException {
}
