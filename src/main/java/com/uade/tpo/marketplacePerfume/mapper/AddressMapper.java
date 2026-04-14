package com.uade.tpo.marketplacePerfume.mapper;

import com.uade.tpo.marketplacePerfume.entity.Address;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.address.InfoAddress;

public final class AddressMapper {
    private AddressMapper() {
    }

    public static Address toNewEntity(InfoAddress dto, User buyer) {
        return Address.builder()
                .street(dto.getStreet())
                .streetNumber(dto.getStreetNumber())
                .city(dto.getCity())
                .state(dto.getState())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .apartment(dto.getApartment())
                .buyer(buyer)
                .build();
    }

    public static void apply(InfoAddress dto, Address address) {
        address.setStreet(dto.getStreet());
        address.setStreetNumber(dto.getStreetNumber());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        address.setApartment(dto.getApartment());
    }

    public static InfoAddress toDto(Address address) {
        InfoAddress dto = new InfoAddress();
        dto.setStreet(address.getStreet());
        dto.setStreetNumber(address.getStreetNumber());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        dto.setApartment(address.getApartment());
        return dto;
    }
}
