package com.smartwatch.leaderboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TelemetryEventDto {

    @NotNull(message = "Step Count Value is required")
    private Integer stepCountValue;

    @NotBlank(message = "Required tag value is missing")
    private String requiredTagValue; // e.g., "GPS", "HRM", "Heart Rate"

    private Long taskId; // Optional associated task being completed
    private Long challengeId; // Optional associated challenge being participated in
    private String date; // format: "YYYY-MM-DD"

    public TelemetryEventDto() {
    }

    public TelemetryEventDto(Integer stepCountValue, String requiredTagValue, Long taskId, Long challengeId, String date) {
        this.stepCountValue = stepCountValue;
        this.requiredTagValue = requiredTagValue;
        this.taskId = taskId;
        this.challengeId = challengeId;
        this.date = date;
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
