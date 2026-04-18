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
import com.uade.tpo.marketplacePerfume.service.user.IUserService;

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
    public ResponseEntity<List<UserProfileResponse>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("users/active")
    public ResponseEntity<List<UserProfileResponse>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @GetMapping("users/inactive")
    public ResponseEntity<List<UserProfileResponse>> getInactiveUsers() {
        return ResponseEntity.ok(userService.getInactiveUsers());
    }

    @GetMapping("users/buyers/active")
    public ResponseEntity<List<UserProfileResponse>> getActiveBuyers() {
        return ResponseEntity.ok(userService.getActiveBuyers());
    }

    @GetMapping("users/sellers/active")
    public ResponseEntity<List<UserProfileResponse>> getActiveSellers() {
        return ResponseEntity.ok(userService.getActiveSellers());
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User successfully deactivated");
    }

    @PutMapping("users/{id}")
    public ResponseEntity<String> reactivateUserById(@PathVariable Long id) {
            userService.reactivateUserById(id);
        return ResponseEntity.ok("User successfully reactivated");
    }
}
