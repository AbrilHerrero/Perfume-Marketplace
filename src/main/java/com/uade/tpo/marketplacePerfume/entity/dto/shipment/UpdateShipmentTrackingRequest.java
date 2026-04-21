package com.uade.tpo.marketplacePerfume.entity.dto.shipment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateShipmentTrackingRequest {
    @NotBlank
    private String trackingNumber;
}
