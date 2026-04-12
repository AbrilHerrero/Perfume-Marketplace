package com.uade.tpo.marketplacePerfume.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdatePasswordRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UpdateUserRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UserProfileResponse;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;
import com.uade.tpo.marketplacePerfume.service.IUserService;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal User currentUser)
            throws UserNonExistanceException {
        return ResponseEntity.ok(userService.getCurrentUserProfile(currentUser));
    }

    @PutMapping("updatePassword")
    public ResponseEntity<Object> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest request,
            @AuthenticationPrincipal User currentUser) throws UserNonExistanceException {
        userService.updatePassword(request, currentUser);
        return ResponseEntity.ok("Password actualizado con exito");
    }

    @PutMapping("updateUser")
    public ResponseEntity<UserProfileResponse> updateUser(
            @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal User currentUser) throws UserNonExistanceException {
        return ResponseEntity.ok(userService.updateUser(request, currentUser));
    }

    @DeleteMapping("deleteUser")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal User currentUser)
            throws UserNonExistanceException {
        userService.deleteUser(currentUser);
        return ResponseEntity.ok("Usuario dado de baja con exito");
    }
}
