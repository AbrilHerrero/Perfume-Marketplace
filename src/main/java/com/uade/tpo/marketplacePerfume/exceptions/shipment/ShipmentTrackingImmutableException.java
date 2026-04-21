package com.uade.tpo.marketplacePerfume.exceptions.shipment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Tracking number is already set and cannot be changed")
public class ShipmentTrackingImmutableException extends RuntimeException {

}
