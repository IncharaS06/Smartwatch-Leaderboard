package com.smartwatch.leaderboard.dto;

import java.time.LocalDateTime;

public class UserBadgeDto {
    private String badgeName;
    private String description;
    private LocalDateTime dateEarned;

    public UserBadgeDto() {
    }

    public UserBadgeDto(String badgeName, String description, LocalDateTime dateEarned) {
        this.badgeName = badgeName;
        this.description = description;
        this.dateEarned = dateEarned;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateEarned() {
        return dateEarned;
    }

    public void setDateEarned(LocalDateTime dateEarned) {
        this.dateEarned = dateEarned;
    }
}
