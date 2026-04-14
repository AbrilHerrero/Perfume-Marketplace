package com.uade.tpo.marketplacePerfume.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.address.InfoAddress;
import com.uade.tpo.marketplacePerfume.service.address.IAddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    private IAddressService addressService;

    @PostMapping("addAddress")
    public ResponseEntity<InfoAddress> addAddress(
            @Valid @RequestBody InfoAddress request,
            @AuthenticationPrincipal User currentUser) {
        InfoAddress created = addressService.addAddress(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("modifyAddress")
    public ResponseEntity<InfoAddress> modifyAddress(
            @Valid @RequestBody InfoAddress request,
            @AuthenticationPrincipal User currentUser) {
        InfoAddress updated = addressService.modifyAddress(request, currentUser);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("deleteAddress")
    public ResponseEntity<String> deleteAddress(@AuthenticationPrincipal User currentUser) {
        addressService.deleteAddress(currentUser);
        return ResponseEntity.ok("Address successfully deleted");
    }
}
