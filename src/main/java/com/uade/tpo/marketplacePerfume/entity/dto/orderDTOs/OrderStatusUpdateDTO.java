package com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusUpdateDTO {

    @NotBlank
    private String status;
}
