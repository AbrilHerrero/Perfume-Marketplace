package com.uade.tpo.marketplacePerfume.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.address.AddressResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.address.CreateAddressRequest;
import com.uade.tpo.marketplacePerfume.service.address.IAddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    private IAddressService addressService;

    @PostMapping("addAddress")
    public ResponseEntity<AddressResponse> addAddress(
            @Valid @RequestBody CreateAddressRequest request,
            @AuthenticationPrincipal User currentUser) {
        AddressResponse created = addressService.addAddress(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("addresses")
    public ResponseEntity<List<AddressResponse>> listAddresses(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(addressService.listAddresses(currentUser));
    }

    @PutMapping("modifyAddress/{addressId}")
    public ResponseEntity<AddressResponse> modifyAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody CreateAddressRequest request,
            @AuthenticationPrincipal User currentUser) {
        AddressResponse updated = addressService.modifyAddress(addressId, request, currentUser);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("deleteAddress/{addressId}")
    public ResponseEntity<String> deleteAddress(
            @PathVariable Long addressId,
            @AuthenticationPrincipal User currentUser) {
        addressService.deleteAddress(addressId, currentUser);
        return ResponseEntity.ok("Address successfully deleted");
    }
}
