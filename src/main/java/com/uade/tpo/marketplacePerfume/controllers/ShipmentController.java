package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.CreateShipmentRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.ShipmentResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.UpdateShipmentStatusRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.UpdateShipmentTrackingRequest;
import com.uade.tpo.marketplacePerfume.service.shipment.IShipmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/shipments")
public class ShipmentController {

    @Autowired
    private IShipmentService shipmentService;

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(shipmentService.getById(id, currentUser));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShipmentResponse> getByOrderId(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(shipmentService.getByOrderId(orderId, currentUser));
    }

    @PostMapping
    public ResponseEntity<ShipmentResponse> create(
            @Valid @RequestBody CreateShipmentRequest request,
            @AuthenticationPrincipal User currentUser) {
        ShipmentResponse created = shipmentService.create(request, currentUser);
        return ResponseEntity.created(URI.create("/api/shipments/" + created.getId())).body(created);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShipmentResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateShipmentStatusRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(shipmentService.updateStatus(id, request, currentUser));
    }

    @PatchMapping("/{id}/tracking")
    public ResponseEntity<ShipmentResponse> setTrackingNumber(
            @PathVariable Long id,
            @Valid @RequestBody UpdateShipmentTrackingRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(shipmentService.setTrackingNumber(id, request, currentUser));
    }
}
