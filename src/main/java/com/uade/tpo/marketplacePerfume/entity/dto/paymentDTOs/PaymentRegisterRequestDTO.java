package com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRegisterRequestDTO {
    @NotNull
    private Long orderId;
    private String methodName;
}
