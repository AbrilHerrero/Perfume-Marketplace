package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdatePasswordRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdateUserRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UserProfileResponse;
import com.uade.tpo.marketplacePerfume.exceptions.AdminUserCannotBeDeletedException;
import com.uade.tpo.marketplacePerfume.exceptions.UserAlreadyActivatedException;
import com.uade.tpo.marketplacePerfume.exceptions.UserAlreadyDeactivatedException;
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
    public void deleteUser(User currentUser) throws UserNonExistanceException, AdminUserCannotBeDeletedException {
        User user = getCurrentManagedUser(currentUser);
        if (user.getRole() == Role.ADMIN) {
            throw new AdminUserCannotBeDeletedException();
        }
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public List<UserProfileResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toProfileResponse)
                .toList();
    }

    @Override
    public List<UserProfileResponse> getActiveUsers() {
        return userRepository.findByActiveTrue().stream()
                .map(UserMapper::toProfileResponse)
                .toList();
    }

    @Override
    public List<UserProfileResponse> getInactiveUsers() {
        return userRepository.findByActiveFalse().stream()
                .map(UserMapper::toProfileResponse)
                .toList();
    }

    @Override
    public List<UserProfileResponse> getActiveBuyers() {
        return userRepository.findByRoleAndActiveTrue(Role.BUYER).stream()
                .map(UserMapper::toProfileResponse)
                .toList();
    }

    @Override
    public List<UserProfileResponse> getActiveSellers() {
        return userRepository.findByRoleAndActiveTrue(Role.SELLER).stream()
                .map(UserMapper::toProfileResponse)
                .toList();
    }

    @Override
    public UserProfileResponse getUserById(Long id) throws UserNonExistanceException {
        User user = userRepository.findById(id)
                .orElseThrow(UserNonExistanceException::new);
        return UserMapper.toProfileResponse(user);
    }

    @Override
    public void deleteUserById(Long id)
            throws UserNonExistanceException, UserAlreadyDeactivatedException, AdminUserCannotBeDeletedException {
        User user = userRepository.findById(id)
                .orElseThrow(UserNonExistanceException::new);
        if (user.getRole() == Role.ADMIN) {
            throw new AdminUserCannotBeDeletedException();
        }
        if (!user.isActive()) {
            throw new UserAlreadyDeactivatedException();
        }
        user.setActive(false);
        userRepository.save(user);
    }

    private User getCurrentManagedUser(User currentUser) throws UserNonExistanceException {
        return userRepository.findById(currentUser.getId())
                .orElseThrow(UserNonExistanceException::new);
    }

    @Override
    public void reactivateUserById(Long id) throws UserNonExistanceException, UserAlreadyActivatedException {
        User user = userRepository.findById(id)
                .orElseThrow(UserNonExistanceException::new);
        if (user.isActive()) {
            throw new UserAlreadyActivatedException();
        }
        user.setActive(true);
        userRepository.save(user);
    }
    
}
