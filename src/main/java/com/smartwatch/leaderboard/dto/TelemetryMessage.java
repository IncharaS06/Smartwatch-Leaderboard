package com.smartwatch.leaderboard.dto;

public class TelemetryMessage {
    private Long userId;
    private Integer stepCountValue;
    private String requiredTagValue;
    private Long taskId;
    private Long challengeId;
    private String date;

    public TelemetryMessage() {
    }

    public TelemetryMessage(Long userId, Integer stepCountValue, String requiredTagValue, Long taskId, Long challengeId, String date) {
        this.userId = userId;
        this.stepCountValue = stepCountValue;
        this.requiredTagValue = requiredTagValue;
        this.taskId = taskId;
        this.challengeId = challengeId;
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStepCountValue() {
        return stepCountValue;
    }

    public void setStepCountValue(Integer stepCountValue) {
        this.stepCountValue = stepCountValue;
    }

    public String getRequiredTagValue() {
        return requiredTagValue;
    }

    public void setRequiredTagValue(String requiredTagValue) {
        this.requiredTagValue = requiredTagValue;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
