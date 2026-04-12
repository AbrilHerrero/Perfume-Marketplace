package com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderResponseDTO {

    // This DTO will be modified in the future to include more information about the order

    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal total;
    private String status;
}
