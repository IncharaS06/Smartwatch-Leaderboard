package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.ChallengeDto;
import com.smartwatch.leaderboard.dto.ChallengeUserResponseDto;
import com.smartwatch.leaderboard.model.Challenge;
import com.smartwatch.leaderboard.model.ChallengeUser;
import com.smartwatch.leaderboard.model.Device;
import com.smartwatch.leaderboard.model.User;
import com.smartwatch.leaderboard.repository.ChallengeRepository;
import com.smartwatch.leaderboard.repository.ChallengeUserRepository;
import com.smartwatch.leaderboard.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final UserRepository userRepository;

    public ChallengeService(
            ChallengeRepository challengeRepository,
            ChallengeUserRepository challengeUserRepository,
            UserRepository userRepository
    ) {
        this.challengeRepository = challengeRepository;
        this.challengeUserRepository = challengeUserRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ChallengeDto createChallenge(ChallengeDto dto) {
        Challenge challenge = Challenge.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .expiryDate(dto.getExpiryDate())
                .requiredFeatureTags(dto.getRequiredFeatureTags())
                .scopeType(dto.getScopeType())
                .city(dto.getCity())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .radiusKm(dto.getRadiusKm())
                .build();
        Challenge saved = challengeRepository.save(challenge);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public ChallengeDto getChallengeById(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found with id: " + id));
        return mapToDto(challenge);
    }

    @Transactional
    public ChallengeDto updateChallenge(Long id, ChallengeDto dto) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found with id: " + id));

        challenge.setName(dto.getName());
        challenge.setDescription(dto.getDescription());
        challenge.setExpiryDate(dto.getExpiryDate());
        challenge.setRequiredFeatureTags(dto.getRequiredFeatureTags());
        challenge.setScopeType(dto.getScopeType());
        challenge.setCity(dto.getCity());
        challenge.setLatitude(dto.getLatitude());
        challenge.setLongitude(dto.getLongitude());
        challenge.setRadiusKm(dto.getRadiusKm());

        Challenge saved = challengeRepository.save(challenge);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public Page<ChallengeUserResponseDto> getPaginatedChallengeUsers(Long challengeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChallengeUser> challengeUsers = challengeUserRepository.findByChallengeId(challengeId, pageable);
        return challengeUsers.map(this::mapToChallengeUserResponseDto);
    }

    @Transactional(readOnly = true)
    public List<ChallengeDto> getDiscoverableChallengesForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Device device = user.getDevice();
        List<Challenge> challenges = challengeRepository.findAll();

        // Perform set-intersection validation to filter challenges visible to the user's registered device
        return challenges.stream()
                .filter(challenge -> {
                    // Challenges with no hardware requirements are open to all
                    if (challenge.getRequiredFeatureTags() == null || challenge.getRequiredFeatureTags().isEmpty()) {
                        return true;
                    }
                    // If challenge has requirements but user has no device, it's not visible
                    if (device == null) {
                        return false;
                    }
                    // The set-intersection check: device must contain all of the challenge's required tags
                    return device.getFeatureTags().containsAll(challenge.getRequiredFeatureTags());
                })
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void joinChallenge(Long challengeId, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found with id: " + challengeId));

        if (challenge.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot join an expired challenge");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (challengeUserRepository.existsByChallengeIdAndUserId(challengeId, userId)) {
            throw new IllegalArgumentException("User has already joined this challenge");
        }

        // Set-intersection validation: Check if user's device supports all required capabilities for the challenge
        Device device = user.getDevice();
        if (device == null) {
            throw new IllegalArgumentException("Please register a smartwatch device to your profile before joining this challenge.");
        }

        if (!device.getFeatureTags().containsAll(challenge.getRequiredFeatureTags())) {
            throw new IllegalArgumentException("Your device '" + device.getName() + "' does not support all required hardware capabilities: " + challenge.getRequiredFeatureTags());
        }

        // Create participation record
        ChallengeUser challengeUser = ChallengeUser.builder()
                .challenge(challenge)
                .user(user)
                .totalScore(0)
                .build();

        challengeUserRepository.save(challengeUser);
    }

    private ChallengeDto mapToDto(Challenge challenge) {
        return new ChallengeDto(
                challenge.getId(),
                challenge.getName(),
                challenge.getDescription(),
                challenge.getExpiryDate(),
                challenge.getRequiredFeatureTags(),
                challenge.getScopeType(),
                challenge.getCity(),
                challenge.getLatitude(),
                challenge.getLongitude(),
                challenge.getRadiusKm()
        );
    }

    private ChallengeUserResponseDto mapToChallengeUserResponseDto(ChallengeUser cu) {
        return new ChallengeUserResponseDto(
                cu.getId(),
                cu.getChallenge().getId(),
                cu.getChallenge().getName(),
                cu.getUser().getId(),
                cu.getUser().getFullName(),
                cu.getTotalScore(),
                cu.getRank()
        );
    }
}
