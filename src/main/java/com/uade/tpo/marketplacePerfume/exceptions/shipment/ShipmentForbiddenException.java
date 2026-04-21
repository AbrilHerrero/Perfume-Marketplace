package com.uade.tpo.marketplacePerfume.exceptions.shipment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "You are not allowed to access this shipment")
public class ShipmentForbiddenException extends RuntimeException {

}
