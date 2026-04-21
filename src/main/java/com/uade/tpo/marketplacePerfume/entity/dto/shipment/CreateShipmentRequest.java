package com.uade.tpo.marketplacePerfume.entity.dto.shipment;

import com.uade.tpo.marketplacePerfume.entity.ShipmentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateShipmentRequest {
    @NotNull
    private Long orderId;
    @NotNull
    private Long addressId;
    private ShipmentStatus status;
    private String trackingNumber;
}
