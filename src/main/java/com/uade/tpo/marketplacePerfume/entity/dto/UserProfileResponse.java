package com.uade.tpo.marketplacePerfume.entity.dto;

import java.time.LocalDate;

import com.uade.tpo.marketplacePerfume.entity.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String telephone;
    private LocalDate registerDate;
    private boolean active;
    private Role role;
}
