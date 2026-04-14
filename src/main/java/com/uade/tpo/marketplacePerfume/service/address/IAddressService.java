package com.uade.tpo.marketplacePerfume.service.address;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.address.AddressResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.address.CreateAddressRequest;

public interface IAddressService {

    AddressResponse addAddress(CreateAddressRequest request, User currentUser);

    List<AddressResponse> listAddresses(User currentUser);

    AddressResponse modifyAddress(Long addressId, CreateAddressRequest request, User currentUser);

    void deleteAddress(Long addressId, User currentUser);
}
