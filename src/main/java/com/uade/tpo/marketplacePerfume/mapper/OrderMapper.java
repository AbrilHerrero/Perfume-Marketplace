package com.uade.tpo.marketplacePerfume.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.OrderItem;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderItemResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderResponseDTO;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponseDTO toResponseDto(Order entity) {
        if (entity == null) {
            return null;
        }

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(entity.getId());
        if (entity.getBuyer() != null) {
            dto.setBuyerId(entity.getBuyer().getId());
            dto.setBuyerEmail(entity.getBuyer().getEmail());
        }
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setTotal(entity.getTotal());
        dto.setStatus(entity.getStatus());
        dto.setItems(toItemResponseDtoList(entity.getOrderItems()));
        return dto;
    }

    public static List<OrderResponseDTO> toResponseDtoList(List<Order> entities) {
        List<OrderResponseDTO> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }
        for (Order entity : entities) {
            dtos.add(toResponseDto(entity));
        }
        return dtos;
    }

    public static OrderItemResponseDTO toItemResponseDto(OrderItem entity) {
        if (entity == null) {
            return null;
        }

        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());

        Sample sample = entity.getSample();
        if (sample != null) {
            dto.setSampleId(sample.getId());
            dto.setSampleDescription(sample.getDescription());
            dto.setUnitPrice(sample.getPrice());
            if (sample.getPrice() != null) {
                dto.setSubtotal(sample.getPrice().multiply(BigDecimal.valueOf(entity.getQuantity())));
            }
        }
        return dto;
    }

    public static List<OrderItemResponseDTO> toItemResponseDtoList(List<OrderItem> entities) {
        List<OrderItemResponseDTO> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }
        for (OrderItem entity : entities) {
            dtos.add(toItemResponseDto(entity));
        }
        return dtos;
    }
}
