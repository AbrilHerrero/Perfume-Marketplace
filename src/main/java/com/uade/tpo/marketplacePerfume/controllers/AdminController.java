package com.uade.tpo.marketplacePerfume.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;

@RestController
@RequestMapping("admin")
public class AdminController {

    @GetMapping("hi")
    public String hi(@AuthenticationPrincipal User user) {
        return "Hi, " + user.getName();
    }
}
