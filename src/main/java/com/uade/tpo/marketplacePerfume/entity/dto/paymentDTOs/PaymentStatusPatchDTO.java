package com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentStatusPatchDTO {
    @NotBlank
    private String status;
}
