package com.uade.tpo.marketplacePerfume.exceptions.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The rating must be between 1 and 5")
public class ReviewInvalidRatingException extends RuntimeException {}