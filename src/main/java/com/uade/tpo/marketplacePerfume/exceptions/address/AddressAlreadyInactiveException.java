package com.uade.tpo.marketplacePerfume.exceptions.address;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "The address is already inactive")
public class AddressAlreadyInactiveException extends RuntimeException {
}
