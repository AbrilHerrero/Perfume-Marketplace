package com.uade.tpo.marketplacePerfume.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.dto.UpdatePasswordRequest;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;
import com.uade.tpo.marketplacePerfume.service.IUserService;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PutMapping("password")
    public ResponseEntity<Object> updatePassword(@RequestBody UpdatePasswordRequest request)
            throws UserNonExistanceException {
        userService.updatePassword(request.getEmail(), request.getPassword());
        return ResponseEntity.ok("Password actualizado con exito");
    }
}
