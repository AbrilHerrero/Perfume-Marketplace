package com.uade.tpo.marketplacePerfume.entity.dto.address;

import lombok.Data;

@Data
public class InfoAddress {
    private String street;
    private String streetNumber;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String apartment;
}
