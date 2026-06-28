package com.uade.tpo.marketplacePerfume.entity.dto.Sample;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class SampleResponseDTO {
    private Long id;
    private BigDecimal price;
    private Integer stock;
    private Integer volumeMl;
    private String description;
    private String imageUrl;
    private Boolean active;
    private Long perfumeId;
    // Perfume display fields denormalized onto the listing so a decant card can
    // be rendered (and filtered by accord) without fetching the perfume catalog.
    private String perfumeName;
    private String perfumeBrand;
    private String perfumeImageUrl;
    private List<String> perfumeAccords;
    private Long sellerId;
    private String sellerName;
    private Double rating;
    private Integer reviewCount;
}