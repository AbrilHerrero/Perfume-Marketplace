package com.uade.tpo.marketplacePerfume.entity.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSavedPaymentMethodRequest {
    @NotBlank
    private String brand;

    @NotBlank
    @Size(min = 4, max = 4)
    @Pattern(regexp = "\\d{4}")
    private String last4;

    @NotBlank
    private String cardholderName;

    @NotBlank
    private String expiry;

    @NotBlank
    private String label;
}
