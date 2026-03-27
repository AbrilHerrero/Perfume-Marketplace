package com.uade.tpo.marketplacePerfume.service;

import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;

public interface IUserService {
    void updatePassword(String email, String newPassword) throws UserNonExistanceException;
}
