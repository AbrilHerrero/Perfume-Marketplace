package com.uade.tpo.marketplacePerfume.entity.dto.Sample;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SampleActiveUpdateDTO {

    @NotNull(message = "active is required")
    private Boolean active;
}
