package com.uade.tpo.marketplacePerfume.mapper;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.userDTOs.SellerSummaryDTO;

public final class UserMapper {

    private UserMapper() {
    }

    public static SellerSummaryDTO toSellerSummary(User entity) {
        if (entity == null) {
            return null;
        }
        SellerSummaryDTO dto = new SellerSummaryDTO();
        dto.setId(entity.getId());
        String display = entity.getName();
        if (entity.getSurname() != null && !entity.getSurname().isBlank()) {
            display = display == null ? entity.getSurname() : display + " " + entity.getSurname();
        }
        dto.setName(display);
        return dto;
    }
}
