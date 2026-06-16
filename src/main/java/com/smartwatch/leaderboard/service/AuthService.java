package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.TokenResponse;
import com.smartwatch.leaderboard.dto.UserLoginDto;
import com.smartwatch.leaderboard.dto.UserRegistrationDto;
import com.smartwatch.leaderboard.model.Role;
import com.smartwatch.leaderboard.model.User;
import com.smartwatch.leaderboard.repository.UserRepository;
import com.smartwatch.leaderboard.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public String register(UserRegistrationDto dto) {
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        User user = User.builder()
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .password(passwordEncoder.encode(dto.getPassword())) // Hashed via BCrypt
                .role(Role.ROLE_USER) // General registration defaults to USER
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

        return TokenResponse.builder()
                .accessToken(token)
                .refreshToken(null) // explicit requirement
                .build();
    }

    public void logout() {
        // Stateless logout clears context
        SecurityContextHolder.clearContext();
    }
}