package com.uade.tpo.marketplacePerfume.exceptions.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The review comment exceeds the maximum allowed length")
public class ReviewCommentTooLongException extends RuntimeException {}
