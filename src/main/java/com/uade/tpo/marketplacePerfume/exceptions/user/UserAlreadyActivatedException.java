package com.uade.tpo.marketplacePerfume.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User is already activated")
public class UserAlreadyActivatedException extends RuntimeException {
    
}
