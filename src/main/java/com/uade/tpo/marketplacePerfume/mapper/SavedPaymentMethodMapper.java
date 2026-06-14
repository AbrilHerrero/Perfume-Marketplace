package com.uade.tpo.marketplacePerfume.mapper;

import com.uade.tpo.marketplacePerfume.entity.SavedPaymentMethod;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.CreateSavedPaymentMethodRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.SavedPaymentMethodResponse;

public final class SavedPaymentMethodMapper {
    private SavedPaymentMethodMapper() {
    }

    public static SavedPaymentMethod toNewEntity(CreateSavedPaymentMethodRequest dto, User buyer) {
        return SavedPaymentMethod.builder()
                .brand(dto.getBrand().trim())
                .last4(dto.getLast4().trim())
                .cardholderName(dto.getCardholderName().trim())
                .expiry(dto.getExpiry().trim())
                .label(dto.getLabel().trim())
                .buyer(buyer)
                .active(true)
                .build();
    }

    public static SavedPaymentMethodResponse toResponse(SavedPaymentMethod entity) {
        SavedPaymentMethodResponse response = new SavedPaymentMethodResponse();
        response.setId(entity.getId());
        response.setBrand(entity.getBrand());
        response.setLast4(entity.getLast4());
        response.setCardholderName(entity.getCardholderName());
        response.setExpiry(entity.getExpiry());
        response.setLabel(entity.getLabel());
        return response;
    }
}
