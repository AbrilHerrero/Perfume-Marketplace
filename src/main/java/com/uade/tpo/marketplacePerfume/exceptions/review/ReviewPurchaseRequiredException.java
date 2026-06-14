package com.uade.tpo.marketplacePerfume.exceptions.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "You can only review samples you have purchased")
public class ReviewPurchaseRequiredException extends RuntimeException {}
