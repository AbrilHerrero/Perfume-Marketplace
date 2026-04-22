package com.uade.tpo.marketplacePerfume.mapper;

import java.math.BigDecimal;

import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.Payment;
import com.uade.tpo.marketplacePerfume.entity.PaymentStatus;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.PaymentResponse;

public final class PaymentMapper {
    private PaymentMapper() {
    }

    public static Payment toNewEntity(Order order, BigDecimal total, PaymentStatus status) {
        return Payment.builder()
                .order(order)
                .total(total)
                .status(status)
                .build();
    }

    public static PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setTotal(payment.getTotal());
        response.setMethodName(payment.getMethodName());
        response.setStatus(payment.getStatus());
        response.setCreatedAt(payment.getCreatedAt());
        if (payment.getOrder() != null) {
            response.setOrderId(payment.getOrder().getId());
        }
        return response;
    }
}
