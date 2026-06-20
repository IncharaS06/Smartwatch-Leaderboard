package com.smartwatch.leaderboard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "challenge_users")
public class ChallengeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore = 0;

    @Column(name = "rank_allotted")
    private Integer rank; // initially null, filled by Milestone 3 Spring Batch job

    public ChallengeUser() {
    }

    public ChallengeUser(Long id, Challenge challenge, User user, Integer totalScore, Integer rank) {
        this.id = id;
        this.challenge = challenge;
        this.user = user;
        this.totalScore = totalScore;
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    // Manual Builder
    public static ChallengeUserBuilder builder() {
        return new ChallengeUserBuilder();
    }

    public static class ChallengeUserBuilder {
        private Long id;
        private Challenge challenge;
        private User user;
        private Integer totalScore = 0;
        private Integer rank;

        public ChallengeUserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ChallengeUserBuilder challenge(Challenge challenge) {
            this.challenge = challenge;
            return this;
        }

        public ChallengeUserBuilder user(User user) {
            this.user = user;
            return this;
        }

        public ChallengeUserBuilder totalScore(Integer totalScore) {
            this.totalScore = totalScore;
            return this;
        }

        public ChallengeUserBuilder rank(Integer rank) {
            this.rank = rank;
            return this;
        }

        public ChallengeUser build() {
            return new ChallengeUser(id, challenge, user, totalScore, rank);
        }
    }
}
