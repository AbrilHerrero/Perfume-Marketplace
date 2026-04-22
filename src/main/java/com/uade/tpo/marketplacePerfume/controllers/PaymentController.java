package com.uade.tpo.marketplacePerfume.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.PaymentResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.UpdatePaymentMethodRequest;
import com.uade.tpo.marketplacePerfume.service.payment.IPaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("payments")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentService.getById(id, currentUser));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getByOrderId(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentService.getByOrderId(orderId, currentUser));
    }

    @PatchMapping("/{id}/method")
    public ResponseEntity<PaymentResponse> updateMethod(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentMethodRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentService.updateMethod(id, request, currentUser));
    }
}
