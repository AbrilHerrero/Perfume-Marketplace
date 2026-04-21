package com.uade.tpo.marketplacePerfume.mapper;

import com.uade.tpo.marketplacePerfume.entity.Address;
import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.Shipment;
import com.uade.tpo.marketplacePerfume.entity.ShipmentStatus;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.ShipmentResponse;

public final class ShipmentMapper {
    private ShipmentMapper() {
    }

    public static Shipment toNewEntity(Order order, Address address, ShipmentStatus status, String trackingNumber) {
        return Shipment.builder()
                .order(order)
                .address(address)
                .status(status)
                .trackingNumber(trackingNumber)
                .build();
    }

    public static ShipmentResponse toResponse(Shipment shipment) {
        ShipmentResponse response = new ShipmentResponse();
        response.setId(shipment.getId());
        response.setShippedAt(shipment.getShippedAt());
        response.setDeliveredAt(shipment.getDeliveredAt());
        response.setStatus(shipment.getStatus());
        response.setTrackingNumber(shipment.getTrackingNumber());
        if (shipment.getOrder() != null) {
            response.setOrderId(shipment.getOrder().getId());
        }
        if (shipment.getAddress() != null) {
            response.setAddressId(shipment.getAddress().getId());
        }
        return response;
    }
}
