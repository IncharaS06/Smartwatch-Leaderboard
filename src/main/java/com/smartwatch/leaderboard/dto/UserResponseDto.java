package com.smartwatch.leaderboard.dto;

import java.util.List;

public class UserResponseDto {
    private Long id;
    private String phone;
    private String email;
    private String fullName;
    private DeviceDto device;
    private Integer totalPoints;
    private Integer accumulatedSteps;
    private List<UserBadgeDto> earnedBadges;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id, String phone, String email, String fullName, DeviceDto device, Integer totalPoints, Integer accumulatedSteps, List<UserBadgeDto> earnedBadges) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.fullName = fullName;
        this.device = device;
        this.totalPoints = totalPoints;
        this.accumulatedSteps = accumulatedSteps;
        this.earnedBadges = earnedBadges;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public DeviceDto getDevice() {
        return device;
    }

    public void setDevice(DeviceDto device) {
        this.device = device;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getAccumulatedSteps() {
        return accumulatedSteps;
    }

    public void setAccumulatedSteps(Integer accumulatedSteps) {
        this.accumulatedSteps = accumulatedSteps;
    }

    public List<UserBadgeDto> getEarnedBadges() {
        return earnedBadges;
    }

    public void setEarnedBadges(List<UserBadgeDto> earnedBadges) {
        this.earnedBadges = earnedBadges;
    }
}
