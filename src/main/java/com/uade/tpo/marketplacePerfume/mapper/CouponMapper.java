package com.uade.tpo.marketplacePerfume.mapper;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Coupon;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponUpdateDTO;

public final class CouponMapper {

    private CouponMapper() {}

    public static CouponResponseDTO toResponseDto(Coupon entity) {
        if (entity == null) return null;
        CouponResponseDTO dto = new CouponResponseDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDiscountType(entity.getDiscountType());
        dto.setDiscountValue(entity.getDiscountValue());
        dto.setValidFrom(entity.getValidFrom());
        dto.setValidUntil(entity.getValidUntil());
        dto.setActive(entity.isActive());
        dto.setCreatedAt(entity.getCreatedAt());
        if (entity.getSeller() != null) dto.setSellerId(entity.getSeller().getId());
        return dto;
    }

    public static Coupon toEntityFromCreate(CouponCreateDTO dto) {
        if (dto == null) return null;
        return Coupon.builder()
                .code(dto.getCode())
                .discountType(dto.getDiscountType())
                .discountValue(dto.getDiscountValue())
                .validFrom(dto.getValidFrom())
                .validUntil(dto.getValidUntil())
                .active(true)
                .build();
    }

    public static void applyUpdate(CouponUpdateDTO dto, Coupon existing) {
        if (dto == null) return;
        if (dto.getDiscountType() != null) existing.setDiscountType(dto.getDiscountType());
        if (dto.getDiscountValue() != null) existing.setDiscountValue(dto.getDiscountValue());
        if (dto.getValidFrom() != null) existing.setValidFrom(dto.getValidFrom());
        if (dto.getValidUntil() != null) existing.setValidUntil(dto.getValidUntil());
        if (dto.getActive() != null) existing.setActive(dto.getActive());
    }

    public static List<CouponResponseDTO> toResponseDtoList(List<Coupon> entities) {
        List<CouponResponseDTO> dtos = new ArrayList<>();
        if (entities != null) {
            for (Coupon entity : entities) {
                dtos.add(toResponseDto(entity));
            }
        }
        return dtos;
    }
}
