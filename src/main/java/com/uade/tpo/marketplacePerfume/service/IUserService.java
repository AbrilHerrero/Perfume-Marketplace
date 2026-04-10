package com.uade.tpo.marketplacePerfume.service;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.UpdatePasswordRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.UpdateUserRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.UserProfileResponse;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;

public interface IUserService {
    void updatePassword(UpdatePasswordRequest newPassword, User currentUser) throws UserNonExistanceException;

    UserProfileResponse updateUser(UpdateUserRequest request, User currentUser)
            throws UserNonExistanceException;

    void deleteUser(User currentUser) throws UserNonExistanceException;
}
