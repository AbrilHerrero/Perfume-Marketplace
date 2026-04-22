package com.uade.tpo.marketplacePerfume.service.payment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.OrderItem;
import com.uade.tpo.marketplacePerfume.entity.OrderStatus;
import com.uade.tpo.marketplacePerfume.entity.Payment;
import com.uade.tpo.marketplacePerfume.entity.PaymentStatus;
import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs.PaymentRegisterRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs.PaymentResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs.PaymentStatusPatchDTO;
import com.uade.tpo.marketplacePerfume.exceptions.order.OrderNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.payment.InvalidPaymentStatusException;
import com.uade.tpo.marketplacePerfume.exceptions.payment.PaymentAccessDeniedException;
import com.uade.tpo.marketplacePerfume.exceptions.payment.PaymentAlreadyExistsException;
import com.uade.tpo.marketplacePerfume.exceptions.payment.PaymentNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.payment.PaymentOrderInvalidStateException;
import com.uade.tpo.marketplacePerfume.mapper.PaymentMapper;
import com.uade.tpo.marketplacePerfume.repository.OrderRepository;
import com.uade.tpo.marketplacePerfume.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getBuyerPayments(User buyer) {
        if (buyer == null || buyer.getRole() != Role.BUYER) {
            throw new PaymentAccessDeniedException();
        }
        return PaymentMapper.toResponseDtoList(
                paymentRepository.findByOrder_Buyer_IdOrderByCreatedAtDesc(buyer.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentStatusOfOrder(Long orderId, User seller) {
        if (seller == null || seller.getRole() != Role.SELLER) {
            throw new PaymentAccessDeniedException();
        }
        Order order = orderRepository.findByIdWithItemsAndSamples(orderId)
                .orElseThrow(OrderNotFoundException::new);
        if (!orderInvolvesSeller(order, seller.getId())) {
            throw new PaymentAccessDeniedException();
        }
        Payment payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(PaymentNotFoundException::new);
        return PaymentMapper.toResponseDto(payment);
    }

    @Override
    @Transactional
    public PaymentResponseDTO registerPayment(PaymentRegisterRequestDTO dto, User buyer) {
        if (buyer == null || buyer.getRole() != Role.BUYER) {
            throw new PaymentAccessDeniedException();
        }
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(OrderNotFoundException::new);
        if (order.getBuyer() == null || !order.getBuyer().getId().equals(buyer.getId())) {
            throw new PaymentAccessDeniedException();
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new PaymentOrderInvalidStateException();
        }
        if (paymentRepository.findByOrder_Id(order.getId()).isPresent()) {
            throw new PaymentAlreadyExistsException();
        }

        String method = dto.getMethodName();
        if (method == null || method.isBlank()) {
            method = "CARD";
        }

        Payment payment = Payment.builder()
                .order(order)
                .total(order.getTotal())
                .methodName(method.trim())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        Payment saved = paymentRepository.save(payment);
        return PaymentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public PaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentStatusPatchDTO dto, User seller) {
        if (seller == null || seller.getRole() != Role.SELLER) {
            throw new PaymentAccessDeniedException();
        }
        Payment payment = paymentRepository.findByIdWithOrderItemsAndSamples(paymentId)
                .orElseThrow(PaymentNotFoundException::new);
        Order order = payment.getOrder();
        if (order == null) {
            throw new PaymentNotFoundException();
        }
        if (!orderInvolvesSeller(order, seller.getId())) {
            throw new PaymentAccessDeniedException();
        }

        final PaymentStatus newStatus;
        try {
            newStatus = PaymentStatus.valueOf(dto.getStatus().trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidPaymentStatusException();
        }

        payment.setStatus(newStatus);
        Payment saved = paymentRepository.save(payment);

        if (newStatus == PaymentStatus.COMPLETED && order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }

        return PaymentMapper.toResponseDto(saved);
    }

    private boolean orderInvolvesSeller(Order order, Long sellerId) {
        if (order.getOrderItems() == null) {
            return false;
        }
        for (OrderItem item : order.getOrderItems()) {
            Sample sample = item.getSample();
            if (sample != null && sample.getSeller() != null
                    && sample.getSeller().getId().equals(sellerId)) {
                return true;
            }
        }
        return false;
    }
}
