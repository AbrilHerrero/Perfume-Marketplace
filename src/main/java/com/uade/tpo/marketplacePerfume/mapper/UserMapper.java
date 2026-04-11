package com.uade.tpo.marketplacePerfume.mapper;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.userDTOs.UserResponseDTO;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDTO toResponseDto(User entity) {
        if (entity == null) {
            return null;
        }
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setTelephone(entity.getTelephone());
        dto.setRegisterDate(entity.getRegisterDate());
        dto.setActive(entity.isActive());
        dto.setRole(entity.getRole());
        return dto;
    }
}
