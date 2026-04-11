package com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs;

import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeResponseDTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.uade.tpo.marketplacePerfume.entity.dto.userDTOs.UserResponseDTO;

@Data
public class SampleResponseDTO {
    private Long id;
    private int volumeMl;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
    private int stock;
    private PerfumeResponseDTO perfume;
    private UserResponseDTO seller;
}
