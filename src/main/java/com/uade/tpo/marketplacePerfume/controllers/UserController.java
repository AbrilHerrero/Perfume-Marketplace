package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.dto.UserDTO;
import com.uade.tpo.marketplacePerfume.exceptions.UserDuplicateException;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;
import com.uade.tpo.marketplacePerfume.service.IUserService;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.UpdatePasswordRequest;;

@RestController
@RequestMapping ("user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity<Object> registerUser (@RequestBody UserDTO userDTO) throws UserDuplicateException{
        User newUser= userService.registUser(userDTO.getId(), userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getTelephone());
       return ResponseEntity.created(URI.create("/user/" + newUser.getId())).body(newUser);
    }

    @GetMapping
    public boolean login (@RequestParam String email, @RequestParam String password) throws UserNonExistanceException{
        return userService.login(email, password);
    }

    @PutMapping("password")
    public ResponseEntity<Object> updatePassword(@RequestBody UpdatePasswordRequest request) throws UserNonExistanceException {

      userService.updatePassword(request.getEmail(), request.getPassword());

    return ResponseEntity.ok("Password actualizado con éxito");
}



    
}
