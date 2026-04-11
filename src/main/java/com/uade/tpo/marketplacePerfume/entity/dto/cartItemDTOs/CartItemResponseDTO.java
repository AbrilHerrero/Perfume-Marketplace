package com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs;

import java.time.LocalDateTime;

import com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs.SampleResponseDTO;

import lombok.Data;

@Data
public class CartItemResponseDTO {

    private Long id;
    private int quantity;
    private LocalDateTime addedAt;
    private SampleResponseDTO sample;
}
