package com.springsecurity.controller;

import com.springsecurity.model.dto.UserAuthTO;
import com.springsecurity.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public UserAuthTO register(@RequestBody UserAuthTO user) {
        log.info("Registering new user: {}", user.getUsername());
        // Logic to register a new user
        return user;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserAuthTO user) {
        log.info("User attempting to login: {}", user.getUsername());
        return  userService.verify(user);
    }
}
