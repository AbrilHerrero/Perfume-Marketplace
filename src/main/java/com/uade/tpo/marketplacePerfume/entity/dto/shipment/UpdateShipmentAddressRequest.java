package com.uade.tpo.marketplacePerfume.entity.dto.shipment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateShipmentAddressRequest {
    @NotNull
    private Long addressId;
}
