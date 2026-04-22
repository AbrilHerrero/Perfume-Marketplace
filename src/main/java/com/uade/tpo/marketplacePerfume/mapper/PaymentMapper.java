package com.uade.tpo.marketplacePerfume.mapper;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Payment;
import com.uade.tpo.marketplacePerfume.entity.dto.paymentDTOs.PaymentResponseDTO;

public final class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentResponseDTO toResponseDto(Payment entity) {
        if (entity == null) {
            return null;
        }
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(entity.getId());
        if (entity.getOrder() != null) {
            dto.setOrderId(entity.getOrder().getId());
        }
        dto.setTotal(entity.getTotal());
        dto.setMethodName(entity.getMethodName());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static List<PaymentResponseDTO> toResponseDtoList(List<Payment> entities) {
        List<PaymentResponseDTO> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }
        for (Payment entity : entities) {
            dtos.add(toResponseDto(entity));
        }
        return dtos;
    }
}
