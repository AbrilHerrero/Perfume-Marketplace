package com.uade.tpo.marketplacePerfume.exceptions.shipment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Tracking number is required to leave PENDING status")
public class ShipmentTrackingRequiredException extends RuntimeException {

}
