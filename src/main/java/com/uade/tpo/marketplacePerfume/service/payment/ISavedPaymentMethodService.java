package com.uade.tpo.marketplacePerfume.service.payment;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.CreateSavedPaymentMethodRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.SavedPaymentMethodResponse;

public interface ISavedPaymentMethodService {
    List<SavedPaymentMethodResponse> listSavedMethods(User buyer);

    SavedPaymentMethodResponse createSavedMethod(CreateSavedPaymentMethodRequest request, User buyer);

    void deleteSavedMethod(Long id, User buyer);
}
