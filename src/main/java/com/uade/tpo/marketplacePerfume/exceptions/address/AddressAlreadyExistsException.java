package com.uade.tpo.marketplacePerfume.exceptions.address;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "The address already exists")
public class AddressAlreadyExistsException extends RuntimeException {

}
