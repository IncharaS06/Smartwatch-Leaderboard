package com.smartwatch.leaderboard.config;

import com.smartwatch.leaderboard.model.Role;
import com.smartwatch.leaderboard.model.User;
import com.smartwatch.leaderboard.model.Reward;
import com.smartwatch.leaderboard.repository.UserRepository;
import com.smartwatch.leaderboard.repository.RewardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RewardRepository rewardRepository;

    public DatabaseSeeder(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RewardRepository rewardRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rewardRepository = rewardRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed a default system Admin if they do not already exist in the database
        String adminPhone = "9999999999";
        if (!userRepository.existsByPhone(adminPhone)) {
            User admin = User.builder()
                    .phone(adminPhone)
                    .email("admin@leaderboard.com")
                    .fullName("System Administrator")
                    .password(passwordEncoder.encode("admin123")) // Hashed securely
                    .role(Role.ROLE_ADMIN)                        // Assigned Admin role!
                    .build();
            userRepository.save(admin);
            System.out.println("\n=============================================================");
            System.out.println(">>> SYSTEM ADMIN SEEDED SUCCESSFULLY!");
            System.out.println(">>> Phone: " + adminPhone);
            System.out.println(">>> Password: admin123");
            System.out.println("=============================================================\n");
        }

        // 2. Seed standard Gamification Rewards / Badges
        seedReward("Novice", 100, "Awarded to players reaching 100 accumulated points.");
        seedReward("Athlete", 500, "Awarded to players reaching 500 accumulated points.");
        seedReward("Champion", 1000, "Awarded to players reaching 1000 accumulated points.");
    }

    private void seedReward(String name, int threshold, String desc) {
        if (rewardRepository.findByBadgeName(name).isEmpty()) {
            Reward r = Reward.builder()
                    .badgeName(name)
                    .pointsThreshold(threshold)
                    .description(desc)
                    .build();
            rewardRepository.save(r);
            System.out.println(">>> Seeded Gamification Badge: " + name + " (Threshold: " + threshold + " points)");
        }
    }
}
