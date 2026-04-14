package com.uade.tpo.marketplacePerfume.service.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Address;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.address.InfoAddress;
import com.uade.tpo.marketplacePerfume.exceptions.address.AddressAlreadyExistsException;
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
    public InfoAddress addAddress(InfoAddress dto, User currentUser) {
        User user = getManagedUser(currentUser);
        if (addressRepository.findByBuyerId(user.getId()).isPresent()) {
            throw new AddressAlreadyExistsException();
        }
        Address address = AddressMapper.toNewEntity(dto, user);
        Address saved = addressRepository.save(address);
        return AddressMapper.toDto(saved);
    }

    @Override
    public InfoAddress modifyAddress(InfoAddress dto, User currentUser) {
        User user = getManagedUser(currentUser);
        Address address = addressRepository.findByBuyerId(user.getId())
                .orElseThrow(AddressNotFoundException::new);
        AddressMapper.apply(dto, address);
        Address saved = addressRepository.save(address);
        return AddressMapper.toDto(saved);
    }

    @Override
    public void deleteAddress(User currentUser) {
        User user = getManagedUser(currentUser);
        Address address = addressRepository.findByBuyerId(user.getId())
                .orElseThrow(AddressNotFoundException::new);
        addressRepository.delete(address);
    }

    private User getManagedUser(User currentUser) {
        return userRepository.findById(currentUser.getId())
                .orElseThrow(UserNonExistanceException::new);
    }
}
