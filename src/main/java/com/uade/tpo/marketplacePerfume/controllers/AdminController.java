package com.uade.tpo.marketplacePerfume.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.user.UserProfileResponse;
import com.uade.tpo.marketplacePerfume.exceptions.AdminUserCannotBeDeletedException;
import com.uade.tpo.marketplacePerfume.exceptions.UserAlreadyActivatedException;
import com.uade.tpo.marketplacePerfume.exceptions.UserAlreadyDeactivatedException;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;
import com.uade.tpo.marketplacePerfume.service.IUserService;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private IUserService userService;

    @GetMapping("hi")
    public String hi(@AuthenticationPrincipal User user) {
        return "Hi, " + user.getName();
    }
    @GetMapping("users")
    public List<UserProfileResponse> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("users/active")
    public List<UserProfileResponse> getActiveUsers() {
        return userService.getActiveUsers();
    }

    @GetMapping("users/inactive")
    public List<UserProfileResponse> getInactiveUsers() {
        return userService.getInactiveUsers();
    }

    @GetMapping("users/buyers/active")
    public List<UserProfileResponse> getActiveBuyers() {
        return userService.getActiveBuyers();
    }

    @GetMapping("users/sellers/active")
    public List<UserProfileResponse> getActiveSellers() {
        return userService.getActiveSellers();
    }

    @GetMapping("users/{id}")
    public UserProfileResponse getUserById(@PathVariable Long id) throws UserNonExistanceException {
        return userService.getUserById(id);
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id)
            throws UserNonExistanceException, UserAlreadyDeactivatedException, AdminUserCannotBeDeletedException {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User successfully deactivated");
    }

    @PutMapping("users/{id}")
    public ResponseEntity<String> reactivateUserById(@PathVariable Long id)
            throws UserNonExistanceException, UserAlreadyActivatedException {
                userService.reactivateUserById(id);
        return ResponseEntity.ok("User successfully reactivated");
    }
}
