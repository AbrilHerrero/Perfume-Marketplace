package com.uade.tpo.marketplacePerfume.service.payment;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs.PaymentRegisterRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs.PaymentResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs.PaymentStatusPatchDTO;

public interface IPaymentService {

    List<PaymentResponseDTO> getBuyerPayments(User buyer);

    PaymentResponseDTO getPaymentStatusOfOrder(Long orderId, User seller);

    PaymentResponseDTO registerPayment(PaymentRegisterRequestDTO dto, User buyer);

    PaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentStatusPatchDTO dto, User seller);
}
