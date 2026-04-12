package com.uade.tpo.marketplacePerfume.service;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdatePasswordRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdateUserRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UserProfileResponse;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;

public interface IUserService {
    UserProfileResponse getCurrentUserProfile(User currentUser) throws UserNonExistanceException;

    void updatePassword(UpdatePasswordRequest newPassword, User currentUser) throws UserNonExistanceException;

    UserProfileResponse updateUser(UpdateUserRequest request, User currentUser)
            throws UserNonExistanceException;

    void deleteUser(User currentUser) throws UserNonExistanceException;
}
