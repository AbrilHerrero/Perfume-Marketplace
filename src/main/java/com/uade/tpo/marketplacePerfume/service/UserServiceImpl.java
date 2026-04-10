package com.uade.tpo.marketplacePerfume.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.UpdatePasswordRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.UpdateUserRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.UserProfileResponse;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(UpdatePasswordRequest newPassword, User currentUser) throws UserNonExistanceException {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(UserNonExistanceException::new);
        user.setPassword(passwordEncoder.encode(newPassword.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserProfileResponse updateUser(UpdateUserRequest request, User currentUser)
            throws UserNonExistanceException {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(UserNonExistanceException::new);

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
        return toProfileResponse(user);
    }

    @Override
    public void deleteUser(User currentUser) throws UserNonExistanceException {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(UserNonExistanceException::new);
        user.setActive(false);
        userRepository.save(user);
    }

    private static UserProfileResponse toProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .registerDate(user.getRegisterDate())
                .active(user.isActive())
                .role(user.getRole())
                .build();
    }
}
