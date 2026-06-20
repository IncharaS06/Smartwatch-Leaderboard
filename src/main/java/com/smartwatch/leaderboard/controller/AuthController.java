package com.smartwatch.leaderboard.controller;

import com.smartwatch.leaderboard.dto.TokenResponse;
import com.smartwatch.leaderboard.dto.UserLoginDto;
import com.smartwatch.leaderboard.dto.UserRegistrationDto;
import com.smartwatch.leaderboard.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // Manual Constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserRegistrationDto dto) {
        String msg = authService.register(dto);
        Map<String, String> response = new HashMap<>();
        response.put("message", msg);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody UserLoginDto dto) {
        TokenResponse tokenResponse = authService.login(dto);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}