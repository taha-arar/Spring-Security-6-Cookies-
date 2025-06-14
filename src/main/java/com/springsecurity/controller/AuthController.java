package com.springsecurity.controller;

import com.springsecurity.model.User;
import com.springsecurity.service.CookieService;
import com.springsecurity.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private final CookieService cookieService;

    @PostMapping("/signup")
    public ResponseEntity<Long> register(@RequestBody User user) {
        log.info("Registering new user: {}", user.getUsername());
        Long id = userService.save(user);
        return ResponseEntity.status(201).body(id);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpServletResponse response) {
        log.info("Login attempt: {}", user.getUsername());
        String token = userService.verify(user);
        cookieService.addJwtTokenToResponse(token, response);
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        cookieService.clearJwtCookie(response);
        return ResponseEntity.ok("Logged out");
    }


}
