package com.uade.tpo.marketplacePerfume.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(String email, String newPassword) throws UserNonExistanceException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNonExistanceException::new);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
