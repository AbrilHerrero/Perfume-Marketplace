package com.uade.tpo.marketplacePerfume.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdatePasswordRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdateUserRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UserProfileResponse;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;
import com.uade.tpo.marketplacePerfume.mapper.UserMapper;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileResponse getCurrentUserProfile(User currentUser) throws UserNonExistanceException {
        User user = getCurrentManagedUser(currentUser);
        return UserMapper.toProfileResponse(user);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request, User currentUser) throws UserNonExistanceException {
        User user = getCurrentManagedUser(currentUser);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserProfileResponse updateUser(UpdateUserRequest request, User currentUser)
            throws UserNonExistanceException {
        User user = getCurrentManagedUser(currentUser);

        if (StringUtils.hasText(request.getName())) {
            user.setName(request.getName());
        }
        if (StringUtils.hasText(request.getSurname())) {
            user.setSurname(request.getSurname());
        }
        if (StringUtils.hasText(request.getTelephone())) {
            user.setTelephone(request.getTelephone());
        }

        userRepository.save(user);
        return UserMapper.toProfileResponse(user);
    }

    @Override
    public void deleteUser(User currentUser) throws UserNonExistanceException {
        User user = getCurrentManagedUser(currentUser);
        user.setActive(false);
        userRepository.save(user);
    }

    private User getCurrentManagedUser(User currentUser) throws UserNonExistanceException {
        return userRepository.findById(currentUser.getId())
                .orElseThrow(UserNonExistanceException::new);
    }
}
