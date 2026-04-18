package com.uade.tpo.marketplacePerfume.exceptions.address;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Address contains empty or invalid fields")
public class AddressInvalidFieldException extends RuntimeException {
}
