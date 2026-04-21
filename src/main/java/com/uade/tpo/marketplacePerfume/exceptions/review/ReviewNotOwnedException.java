package com.uade.tpo.marketplacePerfume.exceptions.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "You are not authorized to modify this review")
public class ReviewNotOwnedException extends RuntimeException {}