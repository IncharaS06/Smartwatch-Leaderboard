package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.TokenResponse;
import com.smartwatch.leaderboard.dto.UserLoginDto;
import com.smartwatch.leaderboard.dto.UserRegistrationDto;
import com.smartwatch.leaderboard.model.Role;
import com.smartwatch.leaderboard.model.User;
import com.smartwatch.leaderboard.repository.UserRepository;
import com.smartwatch.leaderboard.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTests {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);
        authService = new AuthService(userRepository, passwordEncoder, jwtService);
    }

    @Test
    void testRegisterSuccess() {
        UserRegistrationDto dto = new UserRegistrationDto("1234567890", "user@test.com", "Test User", "password");
        when(userRepository.existsByPhone(dto.getPhone())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword");

        String result = authService.register(dto);

        assertEquals("Registration successful", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterThrowsExceptionIfPhoneExists() {
        UserRegistrationDto dto = new UserRegistrationDto("1234567890", "user@test.com", "Test User", "password");
        when(userRepository.existsByPhone(dto.getPhone())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(dto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        UserLoginDto dto = new UserLoginDto("1234567890", "password");
        User user = User.builder()
                .id(1L)
                .phone("1234567890")
                .password("hashedPassword")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByPhone(dto.getPhone())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("mockToken");

        TokenResponse response = authService.login(dto);

        assertNotNull(response);
        assertEquals("mockToken", response.getAccessToken());
        assertNull(response.getRefreshToken());
    }

    @Test
    void testLoginThrowsExceptionWithInvalidPassword() {
        UserLoginDto dto = new UserLoginDto("1234567890", "wrong_password");
        User user = User.builder()
                .id(1L)
                .phone("1234567890")
                .password("hashedPassword")
                .build();

        when(userRepository.findByPhone(dto.getPhone())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.login(dto));
    }

    @Test
    void testLogout() {
        assertDoesNotThrow(() -> authService.logout());
    }
}
