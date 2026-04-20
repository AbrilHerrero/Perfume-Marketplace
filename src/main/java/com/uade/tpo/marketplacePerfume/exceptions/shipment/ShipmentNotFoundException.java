package com.uade.tpo.marketplacePerfume.exceptions.shipment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "The shipment could not be found")
public class ShipmentNotFoundException extends RuntimeException {

}
