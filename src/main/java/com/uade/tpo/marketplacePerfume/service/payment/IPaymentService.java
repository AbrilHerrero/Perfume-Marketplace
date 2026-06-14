package com.uade.tpo.marketplacePerfume.service.payment;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.ConfirmPaymentRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.PaymentResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.UpdatePaymentMethodRequest;

public interface IPaymentService {

    PaymentResponse getById(Long id, User currentUser);

    PaymentResponse getByOrderId(Long orderId, User currentUser);

    PaymentResponse create(Long orderId, User currentUser);

    PaymentResponse updateMethod(Long id, UpdatePaymentMethodRequest request, User currentUser);

    PaymentResponse pay(Long id, ConfirmPaymentRequest request, User currentUser);
}
