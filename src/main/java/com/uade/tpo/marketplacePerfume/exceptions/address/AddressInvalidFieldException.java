package com.uade.tpo.marketplacePerfume.exceptions.address;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Address contains empty fields")
public class AddressInvalidFieldException extends RuntimeException {
}
