package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;
import java.util.List;

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
import com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs.PaymentRegisterRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs.PaymentResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs.PaymentStatusPatchDTO;
import com.uade.tpo.marketplacePerfume.service.payment.IPaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("payment")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/me")
    public ResponseEntity<List<PaymentResponseDTO>> getBuyerPayments(@AuthenticationPrincipal User buyer) {
        return ResponseEntity.ok(paymentService.getBuyerPayments(buyer));
    }

    @GetMapping("/order/{orderId}/status")
    public ResponseEntity<PaymentResponseDTO> getPaymentStatusOfOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User seller) {
        return ResponseEntity.ok(paymentService.getPaymentStatusOfOrder(orderId, seller));
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> registerPayment(
            @Valid @RequestBody PaymentRegisterRequestDTO dto,
            @AuthenticationPrincipal User buyer) {
        PaymentResponseDTO created = paymentService.registerPayment(dto, buyer);
        return ResponseEntity.created(URI.create("/payment/" + created.getId())).body(created);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @PathVariable Long id,
            @Valid @RequestBody PaymentStatusPatchDTO dto,
            @AuthenticationPrincipal User seller) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, dto, seller));
    }
}
