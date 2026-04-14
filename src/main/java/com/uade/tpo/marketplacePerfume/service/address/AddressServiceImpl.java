package com.uade.tpo.marketplacePerfume.service.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Address;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.address.AddressResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.address.CreateAddressRequest;
import com.uade.tpo.marketplacePerfume.exceptions.address.AddressNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.user.UserNonExistanceException;
import com.uade.tpo.marketplacePerfume.mapper.AddressMapper;
import com.uade.tpo.marketplacePerfume.repository.AddressRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressResponse addAddress(CreateAddressRequest request, User currentUser) {
        User user = getManagedUser(currentUser);
        Address address = AddressMapper.toNewEntity(request, user);
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
    public AddressResponse modifyAddress(Long addressId, CreateAddressRequest request, User currentUser) {
        User user = getManagedUser(currentUser);
        Address address = addressRepository.findByIdAndBuyer_Id(addressId, user.getId())
                .orElseThrow(AddressNotFoundException::new);
        if (!address.isActive()) {
            throw new AddressNotFoundException();
        }
        AddressMapper.apply(request, address);
        Address saved = addressRepository.save(address);
        return AddressMapper.toResponse(saved);
    }

    @Override
    public void deleteAddress(Long addressId, User currentUser) {
        User user = getManagedUser(currentUser);
        Address address = addressRepository.findByIdAndBuyer_Id(addressId, user.getId())
                .orElseThrow(AddressNotFoundException::new);
        if (!address.isActive()) {
            return;
        }
        address.setActive(false);
        addressRepository.save(address);
    }

    private User getManagedUser(User currentUser) {
        return userRepository.findById(currentUser.getId())
                .orElseThrow(UserNonExistanceException::new);
    }
}
