package com.uade.tpo.marketplacePerfume.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeNotes {
    private List<String> top;
    private List<String> middle;
    private List<String> base;
}
