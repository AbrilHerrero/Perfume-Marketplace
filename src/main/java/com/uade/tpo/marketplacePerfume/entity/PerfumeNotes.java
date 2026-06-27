package com.uade.tpo.marketplacePerfume.entity;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeNotes {
    @NotEmpty
    private List<String> top;

    @NotEmpty
    private List<String> middle;

    @NotEmpty
    private List<String> base;
}
