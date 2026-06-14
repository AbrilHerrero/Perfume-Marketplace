package com.uade.tpo.marketplacePerfume.entity.dto.payment;

import lombok.Data;

@Data
public class SavedPaymentMethodResponse {
    private Long id;
    private String brand;
    private String last4;
    private String cardholderName;
    private String expiry;
    private String label;
}
