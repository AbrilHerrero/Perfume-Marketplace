package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.CreateSavedPaymentMethodRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.SavedPaymentMethodResponse;
import com.uade.tpo.marketplacePerfume.service.payment.ISavedPaymentMethodService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("payment-methods")
public class SavedPaymentMethodController {

    @Autowired
    private ISavedPaymentMethodService savedPaymentMethodService;

    @GetMapping
    public ResponseEntity<List<SavedPaymentMethodResponse>> listSavedMethods(
            @AuthenticationPrincipal User buyer) {
        return ResponseEntity.ok(savedPaymentMethodService.listSavedMethods(buyer));
    }

    @PostMapping
    public ResponseEntity<SavedPaymentMethodResponse> createSavedMethod(
            @Valid @RequestBody CreateSavedPaymentMethodRequest request,
            @AuthenticationPrincipal User buyer) {
        SavedPaymentMethodResponse created = savedPaymentMethodService.createSavedMethod(request, buyer);
        return ResponseEntity.created(URI.create("/payment-methods/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavedMethod(
            @PathVariable Long id,
            @AuthenticationPrincipal User buyer) {
        savedPaymentMethodService.deleteSavedMethod(id, buyer);
        return ResponseEntity.noContent().build();
    }
}
