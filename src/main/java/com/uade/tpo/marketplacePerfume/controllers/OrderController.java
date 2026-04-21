package com.uade.tpo.marketplacePerfume.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderStatusUpdateDTO;
import com.uade.tpo.marketplacePerfume.service.order.IOrderService;

import jakarta.validation.Valid;

// Order creation is handled by CartController#/cart/checkout, which calls OrderService#createOrder.
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(orderService.getOrdersByBuyer(currentUser.getId()));
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(orderService.getOrderById(id, currentUser));
    }

    @PutMapping("{id}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateDTO dto) {
        return ResponseEntity.ok(orderService.updateStatus(id, dto));
    }

    @PutMapping("{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(orderService.cancelOrder(id, currentUser));
    }
}
