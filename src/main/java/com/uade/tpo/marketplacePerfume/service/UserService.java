package com.uade.tpo.marketplacePerfume.service;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.exceptions.UserDuplicateException;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;

public interface UserService {
    public User registUser (int id, String name, String email, String password,String telephone) throws UserDuplicateException;
    public boolean login (String email, String password) throws UserNonExistanceException;
    public User updateProfile();
    public void updatePassword(String email, String newPassword) throws UserNonExistanceException;

    
}