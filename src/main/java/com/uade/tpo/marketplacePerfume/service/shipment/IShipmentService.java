package com.uade.tpo.marketplacePerfume.service.shipment;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.CreateShipmentRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.ShipmentResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.UpdateShipmentAddressRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.UpdateShipmentStatusRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.UpdateShipmentTrackingRequest;

public interface IShipmentService {

    ShipmentResponse getById(Long id, User currentUser);

    ShipmentResponse getByOrderId(Long orderId, User currentUser);

    ShipmentResponse create(CreateShipmentRequest request, User currentUser);

    ShipmentResponse create(Long orderId, User currentUser);

    ShipmentResponse updateStatus(Long id, UpdateShipmentStatusRequest request, User currentUser);

    ShipmentResponse setTrackingNumber(Long id, UpdateShipmentTrackingRequest request, User currentUser);

    ShipmentResponse updateAddress(Long id, UpdateShipmentAddressRequest request, User currentUser);
}
