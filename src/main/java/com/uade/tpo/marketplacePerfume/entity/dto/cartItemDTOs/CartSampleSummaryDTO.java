package com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.userDTOs.SellerSummaryDTO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartSampleSummaryDTO {

    private Long id;
    private int volumeMl;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private int stock;
    private PerfumeResponseDTO perfume;
    private SellerSummaryDTO seller;
}
