package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.TokenResponse;
import com.smartwatch.leaderboard.dto.UserLoginDto;
import com.smartwatch.leaderboard.dto.UserRegistrationDto;
import com.smartwatch.leaderboard.model.Role;
import com.smartwatch.leaderboard.model.User;
import com.smartwatch.leaderboard.repository.UserRepository;
import com.smartwatch.leaderboard.security.JwtService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Manual constructor
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public String register(UserRegistrationDto dto) {
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        User user = User.builder()
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.ROLE_USER) // General public registration supports ROLE_USER
                .build();

        userRepository.save(user);
        return "Registration successful";
    }

    @Transactional(readOnly = true)
    public TokenResponse login(UserLoginDto dto) {
        User user = userRepository.findByPhone(dto.getPhone())
                .orElseThrow(() -> new IllegalArgumentException("Invalid phone number or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid phone number or password");
        }

        String token = jwtService.generateToken(user);

        // Include user details (including the important userId!) inside the token response
        return TokenResponse.builder()
                .accessToken(token)
                .refreshToken(null)
                .userId(user.getId())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
