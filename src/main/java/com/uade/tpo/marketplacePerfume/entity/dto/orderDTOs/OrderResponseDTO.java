package com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderResponseDTO {

    private Long id;
    private Long buyerId;
    private String buyerEmail;
    private LocalDateTime createdAt;
    private BigDecimal total;
    private String status;
    private List<OrderItemResponseDTO> items;
}
