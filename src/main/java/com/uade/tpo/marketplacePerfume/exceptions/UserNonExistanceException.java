package com.uade.tpo.marketplacePerfume.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User not found")
public class UserNonExistanceException extends RuntimeException {
    
}
