package com.uade.tpo.marketplacePerfume.mapper;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UserProfileResponse;

public final class UserMapper {
    private UserMapper() {
    }
    public static UserProfileResponse toProfileResponse (User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .registerDate(user.getRegisterDate())
                .active(user.isActive())
                .role(user.getRole())
                .build();
    }
}
