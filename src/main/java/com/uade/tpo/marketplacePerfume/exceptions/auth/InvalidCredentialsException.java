package com.uade.tpo.marketplacePerfume.exceptions.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "There is no user for the combination of email and password")
public class InvalidCredentialsException extends RuntimeException {
}
