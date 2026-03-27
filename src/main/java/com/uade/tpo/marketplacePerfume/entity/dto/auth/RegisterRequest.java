package com.uade.tpo.marketplacePerfume.entity.dto.auth;

import com.uade.tpo.marketplacePerfume.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String telephone;
    private Role role;
}
