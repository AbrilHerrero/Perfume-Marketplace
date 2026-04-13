package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdatePasswordRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdateUserRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UserProfileResponse;

public interface IUserService {

    UserProfileResponse getCurrentUserProfile(User currentUser);

    void updatePassword(UpdatePasswordRequest request, User currentUser);

    UserProfileResponse updateUser(UpdateUserRequest request, User currentUser);

    void deleteUser(User currentUser);

    List<UserProfileResponse> getUsers();

    List<UserProfileResponse> getActiveUsers();

    List<UserProfileResponse> getInactiveUsers();

    List<UserProfileResponse> getActiveBuyers();

    List<UserProfileResponse> getActiveSellers();

    UserProfileResponse getUserById(Long id);

    void deleteUserById(Long id);

    void reactivateUserById(Long id);
}
