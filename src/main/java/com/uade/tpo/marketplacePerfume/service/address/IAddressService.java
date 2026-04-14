package com.uade.tpo.marketplacePerfume.service.address;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.address.InfoAddress;

public interface IAddressService {

    InfoAddress addAddress(InfoAddress dto, User currentUser);

    InfoAddress modifyAddress(InfoAddress dto, User currentUser);

    void deleteAddress(User currentUser);
}
