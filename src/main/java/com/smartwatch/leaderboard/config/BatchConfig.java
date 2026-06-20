package com.smartwatch.leaderboard.config;

import com.smartwatch.leaderboard.model.*;
import com.smartwatch.leaderboard.repository.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ChallengeRepository challengeRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;
    private final RewardRepository rewardRepository;
    private final UserBadgeRepository userBadgeRepository;

    public BatchConfig(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ChallengeRepository challengeRepository,
            ChallengeUserRepository challengeUserRepository,
            UserRepository userRepository,
            UserTaskRepository userTaskRepository,
            RewardRepository rewardRepository,
            UserBadgeRepository userBadgeRepository
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.challengeRepository = challengeRepository;
        this.challengeUserRepository = challengeUserRepository;
        this.userRepository = userRepository;
        this.userTaskRepository = userTaskRepository;
        this.rewardRepository = rewardRepository;
        this.userBadgeRepository = userBadgeRepository;
    }

    // ==========================================
    // MILESTONE 3: RANKING BATCH JOB
    // ==========================================

    @Bean
    public Tasklet rankingTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println(">>> Starting Spring Batch Ranking Engine...");

            List<Challenge> allChallenges = challengeRepository.findAll();
            List<Challenge> expiredChallenges = allChallenges.stream()
                    .filter(c -> c.getExpiryDate().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());

            System.out.println(">>> Found " + expiredChallenges.size() + " expired challenges to finalize.");

            for (Challenge challenge : expiredChallenges) {
                System.out.println(">>> Finalizing ranks for expired Challenge: " + challenge.getName());
                List<ChallengeUser> participants = challengeUserRepository.findByChallengeId(challenge.getId());

                if (participants.isEmpty()) {
                    continue;
                }

                participants.sort(Comparator.comparingInt(ChallengeUser::getTotalScore).reversed());

                int currentRank = 1;
                for (ChallengeUser participant : participants) {
                    participant.setRank(currentRank);
                    challengeUserRepository.save(participant);
                    System.out.println(">>> User: " + participant.getUser().getFullName() + " | Score: " + participant.getTotalScore() + " | Assigned Rank: " + currentRank);
                    currentRank++;
                }
            }

            System.out.println(">>> Spring Batch Ranking Engine executed successfully.");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step rankingStep() {
        return new StepBuilder("rankingStep", jobRepository)
                .tasklet(rankingTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Job rankingJob() {
        return new JobBuilder("rankingJob", jobRepository)
                .start(rankingStep())
                .build();
    }

    // ==========================================
    // MILESTONE 4: GAMIFICATION & REWARD BATCH JOB
    // ==========================================

    @Bean
    public Tasklet gamificationTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println(">>> Starting Spring Batch Gamification & Reward Engine...");

            // 1. Fetch all system users
            List<User> users = userRepository.findAll();

            // 2. Fetch all defined rewards / point thresholds
            List<Reward> rewards = rewardRepository.findAll();

            System.out.println(">>> Processing rewards for " + users.size() + " users across " + rewards.size() + " milestones...");

            for (User user : users) {
                // Calculate user's total points in real-time
                int totalPoints = userTaskRepository.findAll().stream()
                        .filter(ut -> ut.getUser().getId().equals(user.getId()))
                        .mapToInt(UserTask::getPointsEarned)
                        .sum();

                System.out.println(">>> User " + user.getFullName() + " has " + totalPoints + " total points.");

                for (Reward reward : rewards) {
                    // Check if user meets the point threshold criteria
                    if (totalPoints >= reward.getPointsThreshold()) {
                        // Check if they already own this badge
                        boolean alreadyHasBadge = userBadgeRepository.existsByUserIdAndRewardId(user.getId(), reward.getId());
                        if (!alreadyHasBadge) {
                            // Issue new Badge!
                            UserBadge newBadge = UserBadge.builder()
                                    .user(user)
                                    .reward(reward)
                                    .dateEarned(LocalDateTime.now())
                                    .build();
                            userBadgeRepository.save(newBadge);
                            System.out.println(">>> [CONGRATS] Issued Badge '" + reward.getBadgeName() + "' to user " + user.getFullName() + "!");
                        }
                    }
                }
            }

            System.out.println(">>> Spring Batch Gamification & Reward Engine executed successfully.");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step gamificationStep() {
        return new StepBuilder("gamificationStep", jobRepository)
                .tasklet(gamificationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Job gamificationJob() {
        return new JobBuilder("gamificationJob", jobRepository)
                .start(gamificationStep())
                .build();
    }
}
