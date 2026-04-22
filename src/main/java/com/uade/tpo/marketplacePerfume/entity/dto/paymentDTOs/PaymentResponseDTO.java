package com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentResponseDTO {
    private Long id;
    private Long orderId;
    private BigDecimal total;
    private String methodName;
    private String status;
    private LocalDateTime createdAt;
}
