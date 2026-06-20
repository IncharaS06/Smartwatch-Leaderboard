package com.smartwatch.leaderboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_badges")
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reward_id", nullable = false)
    private Reward reward;

    @Column(name = "date_earned", nullable = false)
    private LocalDateTime dateEarned;

    public UserBadge() {
    }

    public UserBadge(Long id, User user, Reward reward, LocalDateTime dateEarned) {
        this.id = id;
        this.user = user;
        this.reward = reward;
        this.dateEarned = dateEarned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public LocalDateTime getDateEarned() {
        return dateEarned;
    }

    public void setDateEarned(LocalDateTime dateEarned) {
        this.dateEarned = dateEarned;
    }

    // Manual Builder
    public static UserBadgeBuilder builder() {
        return new UserBadgeBuilder();
    }

    public static class UserBadgeBuilder {
        private Long id;
        private User user;
        private Reward reward;
        private LocalDateTime dateEarned;

        public UserBadgeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBadgeBuilder user(User user) {
            this.user = user;
            return this;
        }

        public UserBadgeBuilder reward(Reward reward) {
            this.reward = reward;
            return this;
        }

        public UserBadgeBuilder dateEarned(LocalDateTime dateEarned) {
            this.dateEarned = dateEarned;
            return this;
        }

        public UserBadge build() {
            return new UserBadge(id, user, reward, dateEarned);
        }
    }
}
