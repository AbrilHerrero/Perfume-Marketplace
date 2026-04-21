package com.uade.tpo.marketplacePerfume.entity.dto.shipment;

import com.uade.tpo.marketplacePerfume.entity.ShipmentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateShipmentStatusRequest {
    @NotNull
    private ShipmentStatus status;
}
