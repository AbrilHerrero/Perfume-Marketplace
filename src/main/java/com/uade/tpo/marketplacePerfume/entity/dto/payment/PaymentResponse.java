package com.uade.tpo.marketplacePerfume.entity.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.uade.tpo.marketplacePerfume.entity.PaymentStatus;

import lombok.Data;

@Data
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private BigDecimal total;
    private String methodName;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}
