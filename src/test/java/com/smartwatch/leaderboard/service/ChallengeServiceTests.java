package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.ChallengeDto;
import com.smartwatch.leaderboard.model.Challenge;
import com.smartwatch.leaderboard.model.ChallengeUser;
import com.smartwatch.leaderboard.model.Device;
import com.smartwatch.leaderboard.model.User;
import com.smartwatch.leaderboard.repository.ChallengeRepository;
import com.smartwatch.leaderboard.repository.ChallengeUserRepository;
import com.smartwatch.leaderboard.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChallengeServiceTests {

    private ChallengeRepository challengeRepository;
    private ChallengeUserRepository challengeUserRepository;
    private UserRepository userRepository;
    private ChallengeService challengeService;

    @BeforeEach
    void setUp() {
        challengeRepository = mock(ChallengeRepository.class);
        challengeUserRepository = mock(ChallengeUserRepository.class);
        userRepository = mock(UserRepository.class);
        challengeService = new ChallengeService(challengeRepository, challengeUserRepository, userRepository);
    }

    @Test
    void testCreateChallenge() {
        ChallengeDto dto = new ChallengeDto(null, "Challenge", "Desc", LocalDateTime.now().plusDays(1), Set.of("GPS"), "GLOBAL", null, null, null, null);
        Challenge saved = new Challenge(1L, "Challenge", "Desc", LocalDateTime.now().plusDays(1), Set.of("GPS"), "GLOBAL", null, null, null, null);

        when(challengeRepository.save(any(Challenge.class))).thenReturn(saved);

        ChallengeDto result = challengeService.createChallenge(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Challenge", result.getName());
    }

    @Test
    void testGetChallengeByIdSuccess() {
        Challenge challenge = new Challenge(1L, "Challenge", "Desc", LocalDateTime.now().plusDays(1), Set.of("GPS"), "GLOBAL", null, null, null, null);
        when(challengeRepository.findById(1L)).thenReturn(Optional.of(challenge));

        ChallengeDto result = challengeService.getChallengeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testJoinChallengeSuccess() {
        Challenge challenge = new Challenge(1L, "GPS Challenge", "Desc", LocalDateTime.now().plusDays(1), Set.of("GPS"), "GLOBAL", null, null, null, null);
        Device userDevice = new Device(10L, "Watch", "Brand", Set.of("GPS", "HRM"));
        User user = new User(5L, "9876543210", "user@test.com", "User Name", "pass", null, userDevice);

        when(challengeRepository.findById(1L)).thenReturn(Optional.of(challenge));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(challengeUserRepository.existsByChallengeIdAndUserId(1L, 5L)).thenReturn(false);

        assertDoesNotThrow(() -> challengeService.joinChallenge(1L, 5L));

        verify(challengeUserRepository, times(1)).save(any(ChallengeUser.class));
    }

    @Test
    void testJoinChallengeThrowsIfExpired() {
        Challenge challenge = new Challenge(1L, "Expired", "Desc", LocalDateTime.now().minusDays(1), Set.of("GPS"), "GLOBAL", null, null, null, null);

        when(challengeRepository.findById(1L)).thenReturn(Optional.of(challenge));

        assertThrows(IllegalArgumentException.class, () -> challengeService.joinChallenge(1L, 5L));
    }

    @Test
    void testJoinChallengeThrowsIfDeviceMissingCapabilities() {
        Challenge challenge = new Challenge(1L, "GPS Challenge", "Desc", LocalDateTime.now().plusDays(1), Set.of("GPS", "HRM"), "GLOBAL", null, null, null, null);
        Device userDevice = new Device(10L, "Watch", "Brand", Set.of("Heart Rate Only")); // Missing GPS and HRM!
        User user = new User(5L, "9876543210", "user@test.com", "User Name", "pass", null, userDevice);

        when(challengeRepository.findById(1L)).thenReturn(Optional.of(challenge));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(challengeUserRepository.existsByChallengeIdAndUserId(1L, 5L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> challengeService.joinChallenge(1L, 5L));
        assertTrue(exception.getMessage().contains("does not support all required hardware capabilities"));
    }

    @Test
    void testJoinChallengeThrowsIfNoDeviceRegistered() {
        Challenge challenge = new Challenge(1L, "GPS Challenge", "Desc", LocalDateTime.now().plusDays(1), Set.of("GPS"), "GLOBAL", null, null, null, null);
        User user = new User(5L, "9876543210", "user@test.com", "User Name", "pass", null, null); // No device registered!

        when(challengeRepository.findById(1L)).thenReturn(Optional.of(challenge));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(challengeUserRepository.existsByChallengeIdAndUserId(1L, 5L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> challengeService.joinChallenge(1L, 5L));
        assertTrue(exception.getMessage().contains("Please register a smartwatch device"));
    }

    @Test
    void testGetDiscoverableChallengesForUser() {
        Device gpsDevice = new Device(10L, "Garmin", "GPS Watch", Set.of("GPS"));
        User user = new User(5L, "9876543210", "user@test.com", "User Name", "pass", null, gpsDevice);

        Challenge compatibleChallenge = new Challenge(100L, "GPS Challenge", "Desc", LocalDateTime.now().plusDays(1), Set.of("GPS"), "GLOBAL", null, null, null, null);
        Challenge incompatibleChallenge = new Challenge(200L, "HRM Challenge", "Desc", LocalDateTime.now().plusDays(1), Set.of("HRM"), "GLOBAL", null, null, null, null);

        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(challengeRepository.findAll()).thenReturn(List.of(compatibleChallenge, incompatibleChallenge));

        List<ChallengeDto> discoverable = challengeService.getDiscoverableChallengesForUser(5L);

        assertEquals(1, discoverable.size());
        assertEquals("GPS Challenge", discoverable.get(0).getName());
    }
}
