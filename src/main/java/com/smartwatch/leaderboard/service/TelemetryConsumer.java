package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.TelemetryMessage;
import com.smartwatch.leaderboard.model.*;
import com.smartwatch.leaderboard.repository.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TelemetryConsumer {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserTaskRepository userTaskRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeUserRepository challengeUserRepository;

    public TelemetryConsumer(
            UserRepository userRepository,
            TaskRepository taskRepository,
            UserTaskRepository userTaskRepository,
            ChallengeRepository challengeRepository,
            ChallengeUserRepository challengeUserRepository
    ) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
        this.challengeRepository = challengeRepository;
        this.challengeUserRepository = challengeUserRepository;
    }

    @KafkaListener(topics = "smartwatch-telemetry", groupId = "smartwatch-leaderboard-group")
    @Transactional
    public void consumeTelemetryEvent(TelemetryMessage message) {
        System.out.println(">>> Consumed Telemetry Event: User=" + message.getUserId() + ", Steps=" + message.getStepCountValue() + ", Tag=" + message.getRequiredTagValue());

        User user = userRepository.findById(message.getUserId()).orElse(null);
        if (user == null) {
            System.err.println(">>> User not found with id: " + message.getUserId());
            return;
        }

        // Validate Device capability (Set intersection validation!)
        Device device = user.getDevice();
        if (device == null) {
            System.err.println(">>> User " + user.getFullName() + " has no device registered! Rejecting telemetry.");
            return;
        }

        if (!device.getFeatureTags().contains(message.getRequiredTagValue())) {
            System.err.println(">>> User device " + device.getName() + " does not support required tag " + message.getRequiredTagValue() + "! Rejecting telemetry.");
            return;
        }

        // Parse date or use current timestamp
        LocalDateTime completionDate = LocalDateTime.now();
        if (message.getDate() != null && !message.getDate().isBlank()) {
            try {
                LocalDate parsedDate = LocalDate.parse(message.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                completionDate = parsedDate.atStartOfDay();
            } catch (Exception e) {
                System.err.println(">>> Failed to parse date: " + message.getDate() + ", using current date instead.");
            }
        }

        // 1. Process Task if taskId is present
        if (message.getTaskId() != null) {
            Task task = taskRepository.findById(message.getTaskId()).orElse(null);
            if (task != null) {
                // Save UserTask details
                UserTask userTask = UserTask.builder()
                        .user(user)
                        .task(task)
                        .stepCount(message.getStepCountValue())
                        .completionDate(completionDate)
                        .pointsEarned(task.getPoints())
                        .build();
                userTaskRepository.save(userTask);
                System.out.println(">>> Successfully completed Task " + task.getTitle() + " for user " + user.getFullName() + ". Points Awarded: " + task.getPoints());
            } else {
                System.err.println(">>> Task not found with id: " + message.getTaskId());
            }
        }

        // 2. Process Challenge if challengeId is present
        if (message.getChallengeId() != null) {
            Challenge challenge = challengeRepository.findById(message.getChallengeId()).orElse(null);
            if (challenge != null) {
                // Check if challenge is expired
                if (challenge.getExpiryDate().isBefore(LocalDateTime.now())) {
                    System.err.println(">>> Challenge " + challenge.getName() + " is already expired! Rejecting participation.");
                    return;
                }

                // Retrieve or create participation entry
                ChallengeUser challengeUser = challengeUserRepository.findByChallengeId(challenge.getId()).stream()
                        .filter(cu -> cu.getUser().getId().equals(user.getId()))
                        .findFirst()
                        .orElse(null);

                if (challengeUser == null) {
                    challengeUser = ChallengeUser.builder()
                            .challenge(challenge)
                            .user(user)
                            .totalScore(message.getStepCountValue())
                            .build();
                } else {
                    challengeUser.setTotalScore(challengeUser.getTotalScore() + message.getStepCountValue());
                }

                challengeUserRepository.save(challengeUser);
                System.out.println(">>> Successfully registered Steps " + message.getStepCountValue() + " to Challenge " + challenge.getName() + " for user " + user.getFullName());
            } else {
                System.err.println(">>> Challenge not found with id: " + message.getChallengeId());
            }
        }
    }
}
