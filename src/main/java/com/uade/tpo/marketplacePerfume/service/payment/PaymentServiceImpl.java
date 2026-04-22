package com.uade.tpo.marketplacePerfume.service.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.Payment;
import com.uade.tpo.marketplacePerfume.entity.PaymentStatus;
import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.PaymentResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.UpdatePaymentMethodRequest;
import com.uade.tpo.marketplacePerfume.exceptions.order.OrderNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.payment.PaymentAlreadyExistsException;
import com.uade.tpo.marketplacePerfume.exceptions.payment.PaymentForbiddenException;
import com.uade.tpo.marketplacePerfume.exceptions.payment.PaymentNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.payment.PaymentNotPendingException;
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
    public PaymentResponse getById(Long id, User currentUser) {
        Payment payment = paymentRepository.findFetchedById(id)
                .orElseThrow(PaymentNotFoundException::new);
        assertCanViewPayment(currentUser, payment.getOrder());
        return PaymentMapper.toResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getByOrderId(Long orderId, User currentUser) {
        Payment payment = paymentRepository.findFetchedByOrderId(orderId)
                .orElseThrow(PaymentNotFoundException::new);
        assertCanViewPayment(currentUser, payment.getOrder());
        return PaymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse create(Long orderId, User currentUser) {
        Order order = orderRepository.findByIdWithBuyerAndItems(orderId)
                .orElseThrow(OrderNotFoundException::new);
        assertCanCreatePayment(currentUser, order);
        if (paymentRepository.existsByOrder_Id(order.getId())) {
            throw new PaymentAlreadyExistsException();
        }
        BigDecimal total = order.getTotal() != null ? order.getTotal() : BigDecimal.ZERO;
        Payment entity = PaymentMapper.toNewEntity(order, total, PaymentStatus.PENDING);
        entity.setCreatedAt(LocalDateTime.now());
        Payment saved = paymentRepository.save(entity);
        return PaymentMapper.toResponse(paymentRepository.findFetchedById(saved.getId())
                .orElse(saved));
    }

    @Override
    @Transactional
    public PaymentResponse updateMethod(Long id, UpdatePaymentMethodRequest request, User currentUser) {
        Payment payment = paymentRepository.findFetchedById(id)
                .orElseThrow(PaymentNotFoundException::new);
        assertCanCreatePayment(currentUser, payment.getOrder());
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new PaymentNotPendingException();
        }
        payment.setMethodName(request.getMethodName().trim());
        return PaymentMapper.toResponse(payment);
    }

    private void assertCanViewPayment(User user, Order order) {
        if (user.getRole() == Role.ADMIN) {
            return;
        }
        if (user.getRole() == Role.BUYER
                && order.getBuyer() != null
                && order.getBuyer().getId().equals(user.getId())) {
            return;
        }
        throw new PaymentForbiddenException();
    }

    private void assertCanCreatePayment(User user, Order order) {
        if (user.getRole() == Role.ADMIN) {
            return;
        }
        if (user.getRole() == Role.BUYER
                && order.getBuyer() != null
                && order.getBuyer().getId().equals(user.getId())) {
            return;
        }
        throw new PaymentForbiddenException();
    }
}
