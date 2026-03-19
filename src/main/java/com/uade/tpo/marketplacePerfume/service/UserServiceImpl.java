package com.uade.tpo.marketplacePerfume.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.exceptions.UserDuplicateException;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

@Service
public class UserServiceImpl implements IUserService {
    private UserRepository userRepository;
    
    public UserServiceImpl (){
        this.userRepository = new UserRepository();
    }

    @Override
    public User registUser(int id, String name, String email, String password, String telephone) throws UserDuplicateException {
        ArrayList<User> users = userRepository.getAllUsers();
        if (users.stream().anyMatch(
            user -> user.getEmail().equals(email)
        )) {
        throw new UserDuplicateException();
    }
        return userRepository.createUser(id, name, email, password, telephone);
    }

   
    /*public boolean login(String email, String password) throws UserNonExistanceException {
        ArrayList<User> users = userRepository.getAllUsers();
        if (users.stream()
                .anyMatch(user -> user.getEmail().equals(email) && user.getPassword().equals(password))) {
            return true;
        }
        throw new UserNonExistanceException();
    }*/
    @Override
    public boolean login(String email, String password) throws UserNonExistanceException {
        ArrayList<User> users = userRepository.getAllUsers();
    
        User user = users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        if (user == null) {
            throw new UserNonExistanceException();
        }
        return user.getPassword().equals(password);
    }

    @Override
    public User updateProfile() {
        throw new UnsupportedOperationException("Unimplemented method 'updateProfile'");
    }

    public void updatePassword(String email, String newPassword) throws UserNonExistanceException {
        boolean exists = userRepository.getAllUsers()
                .stream()
                .anyMatch(user -> user.getEmail().equals(email));
    
        if (!exists) {
            throw new UserNonExistanceException();
        }
    
        userRepository.updatePassword(email, newPassword);
    }

}