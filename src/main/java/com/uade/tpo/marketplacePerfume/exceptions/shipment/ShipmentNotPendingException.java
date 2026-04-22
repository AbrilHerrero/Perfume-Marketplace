package com.uade.tpo.marketplacePerfume.exceptions.shipment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,
        reason = "The shipment can only be modified while it is PENDING")
public class ShipmentNotPendingException extends RuntimeException {
}
