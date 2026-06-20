package com.smartwatch.leaderboard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "badge_name", nullable = false, unique = true)
    private String badgeName; // Novice, Athlete, Champion, etc.

    @Column(name = "points_threshold", nullable = false)
    private Integer pointsThreshold;

    private String description;

    public Reward() {
    }

    public Reward(Long id, String badgeName, Integer pointsThreshold, String description) {
        this.id = id;
        this.badgeName = badgeName;
        this.pointsThreshold = pointsThreshold;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public Integer getPointsThreshold() {
        return pointsThreshold;
    }

    public void setPointsThreshold(Integer pointsThreshold) {
        this.pointsThreshold = pointsThreshold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Manual Builder
    public static RewardBuilder builder() {
        return new RewardBuilder();
    }

    public static class RewardBuilder {
        private Long id;
        private String badgeName;
        private Integer pointsThreshold;
        private String description;

        public RewardBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RewardBuilder badgeName(String badgeName) {
            this.badgeName = badgeName;
            return this;
        }

        public RewardBuilder pointsThreshold(Integer pointsThreshold) {
            this.pointsThreshold = pointsThreshold;
            return this;
        }

        public RewardBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Reward build() {
            return new Reward(id, badgeName, pointsThreshold, description);
        }
    }
}
