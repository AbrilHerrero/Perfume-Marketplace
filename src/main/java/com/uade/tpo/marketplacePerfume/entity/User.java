package com.uade.tpo.marketplacePerfume.entity;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class  User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String telephone;
    private LocalDate registerDate;
    private boolean active;
}
