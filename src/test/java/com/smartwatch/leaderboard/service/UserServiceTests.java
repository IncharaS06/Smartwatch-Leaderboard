package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.UserResponseDto;
import com.smartwatch.leaderboard.model.Device;
import com.smartwatch.leaderboard.model.User;
import com.smartwatch.leaderboard.model.UserTask;
import com.smartwatch.leaderboard.repository.ChallengeUserRepository;
import com.smartwatch.leaderboard.repository.UserRepository;
import com.smartwatch.leaderboard.repository.UserBadgeRepository;
import com.smartwatch.leaderboard.repository.UserTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTests {

    private UserRepository userRepository;
    private UserTaskRepository userTaskRepository;
    private ChallengeUserRepository challengeUserRepository;
    private UserBadgeRepository userBadgeRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userTaskRepository = mock(UserTaskRepository.class);
        challengeUserRepository = mock(ChallengeUserRepository.class);
        userBadgeRepository = mock(UserBadgeRepository.class);
        userService = new UserService(userRepository, userTaskRepository, challengeUserRepository, userBadgeRepository);
    }

    @Test
    void testGetUserDetailsSuccess() {
        Device device = new Device(10L, "Forerunner", "Garmin", Set.of("GPS"));
        User user = new User(1L, "9876543210", "athlete@test.com", "Test Athlete", "password", null, device);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userTaskRepository.findAll()).thenReturn(Collections.emptyList());
        when(challengeUserRepository.findAll()).thenReturn(Collections.emptyList());
        when(userBadgeRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        UserResponseDto result = userService.getUserDetails(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Athlete", result.getFullName());
        assertNotNull(result.getDevice());
        assertEquals("Forerunner", result.getDevice().getName());
        assertEquals(0, result.getTotalPoints());
        assertEquals(0, result.getAccumulatedSteps());
        assertTrue(result.getEarnedBadges().isEmpty());
    }

    @Test
    void testRegisterUserDevice() {
        User user = new User(1L, "9876543210", "athlete@test.com", "Test Athlete", "password", null, null);
        Device device = new Device(10L, "Forerunner", "Garmin", Set.of("GPS"));
        com.smartwatch.leaderboard.repository.DeviceRepository deviceRepository = mock(com.smartwatch.leaderboard.repository.DeviceRepository.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(deviceRepository.findById(10L)).thenReturn(Optional.of(device));

        userService.registerUserDevice(1L, 10L, null, deviceRepository);

        assertNotNull(user.getDevice());
        assertEquals(10L, user.getDevice().getId());
        verify(userRepository, times(1)).save(user);
    }
}
