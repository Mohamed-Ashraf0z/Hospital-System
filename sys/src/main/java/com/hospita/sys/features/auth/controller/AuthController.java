package com.hospita.sys.features.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospita.sys.features.auth.entity.User;
import com.hospita.sys.features.auth.service.AuthService;

import jakarta.validation.Valid;
// @CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@Valid @RequestBody User user){
        return authService.login(user);
    }

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody User user){
        return authService.signup(user);
    }

}
