package com.uade.tpo.marketplacePerfume.entity.dto;
import java.time.LocalDate;

import lombok.Data;
@Data
public class UserDTO {
    private int id;
    private String name;
    private String email;
    private String password;
    private String telephone;
    private LocalDate registerDate;
    private boolean active;
}
