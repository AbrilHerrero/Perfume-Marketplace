package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdatePasswordRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdateUserRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UserProfileResponse;
import com.uade.tpo.marketplacePerfume.exceptions.AdminUserCannotBeDeletedException;
import com.uade.tpo.marketplacePerfume.exceptions.UserAlreadyDeactivatedException;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;

public interface IUserService {

    UserProfileResponse getCurrentUserProfile(User currentUser) throws UserNonExistanceException;

    void updatePassword(UpdatePasswordRequest request, User currentUser) throws UserNonExistanceException;

    UserProfileResponse updateUser(UpdateUserRequest request, User currentUser)
            throws UserNonExistanceException;

    void deleteUser(User currentUser) throws UserNonExistanceException;

    List<UserProfileResponse> getUsers();

    List<UserProfileResponse> getActiveUsers();

    List<UserProfileResponse> getInactiveUsers();

    List<UserProfileResponse> getActiveBuyers();

    List<UserProfileResponse> getActiveSellers();

    UserProfileResponse getUserById(Long id) throws UserNonExistanceException;

    void deleteUserById(Long id)
            throws UserNonExistanceException, UserAlreadyDeactivatedException, AdminUserCannotBeDeletedException;
}
