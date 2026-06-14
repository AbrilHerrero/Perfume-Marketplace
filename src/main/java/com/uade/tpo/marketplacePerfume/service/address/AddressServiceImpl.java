package com.uade.tpo.marketplacePerfume.service.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Address;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.address.AddressResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.address.CreateAddressRequest;
import com.uade.tpo.marketplacePerfume.exceptions.address.AddressAlreadyInactiveException;
import com.uade.tpo.marketplacePerfume.exceptions.address.AddressInvalidFieldException;
import com.uade.tpo.marketplacePerfume.exceptions.address.AddressNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.user.UserNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.AddressMapper;
import com.uade.tpo.marketplacePerfume.repository.AddressRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;



@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressResponse addAddress(CreateAddressRequest request, User currentUser) {
        validateFields(request);
        User user = getManagedUser(currentUser);
        List<Address> existing = addressRepository.findAllByBuyer_IdAndActiveTrueOrderByIdAsc(user.getId());
        Address address = AddressMapper.toNewEntity(request, user);

        // First address is always the default; otherwise honour the requested flag.
        boolean makeDefault = request.isDefaultAddress() || existing.isEmpty();
        if (makeDefault) {
            clearOtherDefaults(user.getId(), null);
        }
        address.setDefaultAddress(makeDefault);

        Address saved = addressRepository.save(address);
        return AddressMapper.toResponse(saved);
    }

    @Override
    public List<AddressResponse> listAddresses(User currentUser) {
        User user = getManagedUser(currentUser);
        return addressRepository.findAllByBuyer_IdAndActiveTrueOrderByIdAsc(user.getId()).stream()
                .map(AddressMapper::toResponse)
                .toList();
    }

    @Override
    public AddressResponse getAddressById(Long addressId, User currentUser) {
        User user = getManagedUser(currentUser);
        Address address = addressRepository.findByIdAndBuyer_IdAndActiveTrue(addressId, user.getId())
                .orElseThrow(AddressNotFoundException::new);
        return AddressMapper.toResponse(address);
    }

    @Override
    public AddressResponse modifyAddress(Long addressId, CreateAddressRequest request, User currentUser) {
        validateFields(request);
        User user = getManagedUser(currentUser);
        Address address = addressRepository.findByIdAndBuyer_IdAndActiveTrue(addressId, user.getId())
                .orElseThrow(AddressNotFoundException::new);
        AddressMapper.apply(request, address);
        if (request.isDefaultAddress()) {
            clearOtherDefaults(user.getId(), addressId);
            address.setDefaultAddress(true);
        }
        Address saved = addressRepository.save(address);
        return AddressMapper.toResponse(saved);
    }

    @Override
    public AddressResponse setDefaultAddress(Long addressId, User currentUser) {
        User user = getManagedUser(currentUser);
        Address address = addressRepository.findByIdAndBuyer_IdAndActiveTrue(addressId, user.getId())
                .orElseThrow(AddressNotFoundException::new);
        clearOtherDefaults(user.getId(), addressId);
        address.setDefaultAddress(true);
        return AddressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long addressId, User currentUser) {
        User user = getManagedUser(currentUser);
        Address address = addressRepository.findByIdAndBuyer_Id(addressId, user.getId())
                .orElseThrow(AddressNotFoundException::new);
        if (!address.isActive()) {
            throw new AddressAlreadyInactiveException();
        }
        boolean wasDefault = address.isDefaultAddress();
        address.setActive(false);
        address.setDefaultAddress(false);
        addressRepository.save(address);

        // Keep exactly one default: promote the oldest remaining address.
        if (wasDefault) {
            List<Address> remaining = addressRepository.findAllByBuyer_IdAndActiveTrueOrderByIdAsc(user.getId());
            if (!remaining.isEmpty()) {
                Address promoted = remaining.get(0);
                promoted.setDefaultAddress(true);
                addressRepository.save(promoted);
            }
        }
    }

    private void clearOtherDefaults(Long buyerId, Long keepAddressId) {
        for (Address address : addressRepository.findAllByBuyer_IdAndActiveTrueOrderByIdAsc(buyerId)) {
            if (address.isDefaultAddress() && !address.getId().equals(keepAddressId)) {
                address.setDefaultAddress(false);
                addressRepository.save(address);
            }
        }
    }

    private void validateFields(CreateAddressRequest request) {
        if (isBlank(request.getStreet()) ||
                isBlank(request.getStreetNumber()) ||
                isBlank(request.getCity()) ||
                isBlank(request.getState()) ||
                isBlank(request.getPostalCode()) ||
                isBlank(request.getCountry())) {
            throw new AddressInvalidFieldException();
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private User getManagedUser(User currentUser) {
        return userRepository.findById(currentUser.getId())
                .orElseThrow(UserNotFoundException::new);
    }
}
