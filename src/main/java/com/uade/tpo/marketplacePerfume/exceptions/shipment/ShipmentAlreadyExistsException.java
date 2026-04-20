package com.uade.tpo.marketplacePerfume.exceptions.shipment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "This order already has a shipment")
public class ShipmentAlreadyExistsException extends RuntimeException {

}
