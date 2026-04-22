package com.uade.tpo.marketplacePerfume.exceptions.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "A review already exists for this sample")
public class ReviewAlreadyExistsException extends RuntimeException {}