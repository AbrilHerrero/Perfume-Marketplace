package com.uade.tpo.marketplacePerfume.entity.dto.shipment;

import java.time.LocalDateTime;

import com.uade.tpo.marketplacePerfume.entity.ShipmentStatus;

import lombok.Data;

@Data
public class ShipmentResponse {
    private Long id;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private ShipmentStatus status;
    private String trackingNumber;
    private Long orderId;
    private Long addressId;
}
