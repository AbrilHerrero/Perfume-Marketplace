package com.uade.tpo.marketplacePerfume.entity.dto.userDTOs;

import java.time.LocalDate;

import com.uade.tpo.marketplacePerfume.entity.Role;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String telephone;
    private LocalDate registerDate;
    private boolean active;
    private Role role;
}
