package com.uade.tpo.marketplacePerfume.mapper;

import com.uade.tpo.marketplacePerfume.entity.Address;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.address.AddressResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.address.CreateAddressRequest;

public final class AddressMapper {
    private AddressMapper() {
    }

    public static Address toNewEntity(CreateAddressRequest dto, User buyer) {
        return Address.builder()
                .street(dto.getStreet())
                .streetNumber(dto.getStreetNumber())
                .city(dto.getCity())
                .state(dto.getState())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .apartment(dto.getApartment())
                .buyer(buyer)
                .active(true)
                .build();
    }

    public static void apply(CreateAddressRequest dto, Address address) {
        address.setStreet(dto.getStreet());
        address.setStreetNumber(dto.getStreetNumber());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        address.setApartment(dto.getApartment());
    }

    public static AddressResponse toResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setActive(address.isActive());
        response.setStreet(address.getStreet());
        response.setStreetNumber(address.getStreetNumber());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPostalCode(address.getPostalCode());
        response.setCountry(address.getCountry());
        response.setApartment(address.getApartment());
        return response;
    }
}
