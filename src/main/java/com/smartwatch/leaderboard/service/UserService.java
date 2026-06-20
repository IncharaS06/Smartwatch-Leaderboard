package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.DeviceDto;
import com.smartwatch.leaderboard.dto.UserBadgeDto;
import com.smartwatch.leaderboard.dto.UserResponseDto;
import com.smartwatch.leaderboard.model.Device;
import com.smartwatch.leaderboard.model.User;
import com.smartwatch.leaderboard.repository.UserRepository;
import com.smartwatch.leaderboard.repository.UserTaskRepository;
import com.smartwatch.leaderboard.repository.ChallengeUserRepository;
import com.smartwatch.leaderboard.repository.UserBadgeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final UserBadgeRepository userBadgeRepository;

    public UserService(
            UserRepository userRepository,
            UserTaskRepository userTaskRepository,
            ChallengeUserRepository challengeUserRepository,
            UserBadgeRepository userBadgeRepository
    ) {
        this.userRepository = userRepository;
        this.userTaskRepository = userTaskRepository;
        this.challengeUserRepository = challengeUserRepository;
        this.userBadgeRepository = userBadgeRepository;
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Calculate aggregate performance metrics
        int totalPoints = userTaskRepository.findAll().stream()
                .filter(ut -> ut.getUser().getId().equals(userId))
                .mapToInt(ut -> ut.getPointsEarned())
                .sum();

        int stepsFromTasks = userTaskRepository.findAll().stream()
                .filter(ut -> ut.getUser().getId().equals(userId) && ut.getStepCount() != null)
                .mapToInt(ut -> ut.getStepCount())
                .sum();

        int stepsFromChallenges = challengeUserRepository.findAll().stream()
                .filter(cu -> cu.getUser().getId().equals(userId))
                .mapToInt(cu -> cu.getTotalScore())
                .sum();

        int accumulatedSteps = stepsFromTasks + stepsFromChallenges;

        DeviceDto deviceDto = null;
        if (user.getDevice() != null) {
            Device dev = user.getDevice();
            deviceDto = new DeviceDto(dev.getId(), dev.getName(), dev.getBrand(), dev.getFeatureTags());
        }

        // Fetch user's earned badges
        List<UserBadgeDto> earnedBadges = userBadgeRepository.findByUserId(userId).stream()
                .map(ub -> new UserBadgeDto(
                        ub.getReward().getBadgeName(),
                        ub.getReward().getDescription(),
                        ub.getDateEarned()
                ))
                .collect(Collectors.toList());

        return new UserResponseDto(
                user.getId(),
                user.getPhone(),
                user.getEmail(),
                user.getFullName(),
                deviceDto,
                totalPoints,
                accumulatedSteps,
                earnedBadges
        );
    }

    @Transactional
    public void registerUserDevice(Long userId, Long deviceId, DeviceService deviceService, com.smartwatch.leaderboard.repository.DeviceRepository deviceRepository) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + deviceId));

        user.setDevice(device);
        userRepository.save(user);
    }
}
