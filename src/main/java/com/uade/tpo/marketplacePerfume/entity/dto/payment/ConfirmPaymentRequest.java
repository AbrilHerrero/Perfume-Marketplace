package com.uade.tpo.marketplacePerfume.entity.dto.payment;

import lombok.Data;

@Data
public class ConfirmPaymentRequest {
    private String cardNumber;
    private String expiry;
}
