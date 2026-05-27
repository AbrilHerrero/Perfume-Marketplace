package com.uade.tpo.marketplacePerfume.mapper;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.CouponRedemption;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponRedemptionResponseDTO;

public final class CouponRedemptionMapper {

    private CouponRedemptionMapper() {}

    public static CouponRedemptionResponseDTO toResponseDto(CouponRedemption entity) {
        if (entity == null) return null;
        CouponRedemptionResponseDTO dto = new CouponRedemptionResponseDTO();
        dto.setId(entity.getId());
        dto.setDiscountAmount(entity.getDiscountAmount());
        dto.setRedeemedAt(entity.getRedeemedAt());
        if (entity.getCoupon() != null) {
            dto.setCouponId(entity.getCoupon().getId());
            dto.setCouponCode(entity.getCoupon().getCode());
        }
        if (entity.getBuyer() != null) {
            dto.setBuyerId(entity.getBuyer().getId());
            dto.setBuyerEmail(entity.getBuyer().getEmail());
        }
        if (entity.getOrder() != null) {
            dto.setOrderId(entity.getOrder().getId());
        }
        return dto;
    }

    public static List<CouponRedemptionResponseDTO> toResponseDtoList(List<CouponRedemption> entities) {
        List<CouponRedemptionResponseDTO> dtos = new ArrayList<>();
        if (entities != null) {
            for (CouponRedemption entity : entities) {
                dtos.add(toResponseDto(entity));
            }
        }
        return dtos;
    }
}
