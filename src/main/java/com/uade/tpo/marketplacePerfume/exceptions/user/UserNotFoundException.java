package com.uade.tpo.marketplacePerfume.exceptions.user;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "No se ha encontrado el usuario")
public class UserNotFoundException extends RuntimeException {
    
}
