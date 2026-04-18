package com.uade.tpo.marketplacePerfume.entity.dto.Sample;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockUpdateDTO {

    @NotNull(message = "stock is required")
    @Min(value = 0, message = "stock must be zero or positive")
    private Integer stock;
}
