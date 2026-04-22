package com.uade.tpo.marketplacePerfume.entity.dto.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePaymentMethodRequest {
    @NotBlank
    private String methodName;
}
