package com.smartwatch.leaderboard.dto;

public class ChallengeUserResponseDto {
    private Long id;
    private Long challengeId;
    private String challengeName;
    private Long userId;
    private String userFullName;
    private Integer totalScore;
    private Integer rank;

    public ChallengeUserResponseDto() {
    }

    public ChallengeUserResponseDto(Long id, Long challengeId, String challengeName, Long userId, String userFullName, Integer totalScore, Integer rank) {
        this.id = id;
        this.challengeId = challengeId;
        this.challengeName = challengeName;
        this.userId = userId;
        this.userFullName = userFullName;
        this.totalScore = totalScore;
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
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
}
