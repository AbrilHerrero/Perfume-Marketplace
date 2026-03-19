package com.uade.tpo.marketplacePerfume.entity.dto;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String email;
    private String password;
    
}
