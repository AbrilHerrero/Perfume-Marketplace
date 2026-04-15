package com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OrderCreateDTO {

    @NotEmpty
    @Valid
    private List<OrderItemCreateDTO> items;
}
