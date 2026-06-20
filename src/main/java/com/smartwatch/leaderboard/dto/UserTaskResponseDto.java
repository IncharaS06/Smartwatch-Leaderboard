package com.smartwatch.leaderboard.dto;

import java.time.LocalDateTime;

public class UserTaskResponseDto {
    private Long id;
    private Long userId;
    private String userFullName;
    private Long taskId;
    private String taskTitle;
    private Integer stepCount;
    private LocalDateTime completionDate;
    private Integer pointsEarned;

    public UserTaskResponseDto() {
    }

    public UserTaskResponseDto(Long id, Long userId, String userFullName, Long taskId, String taskTitle, Integer stepCount, LocalDateTime completionDate, Integer pointsEarned) {
        this.id = id;
        this.userId = userId;
        this.userFullName = userFullName;
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.stepCount = stepCount;
        this.completionDate = completionDate;
        this.pointsEarned = pointsEarned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public Integer getStepCount() {
        return stepCount;
    }

    public void setStepCount(Integer stepCount) {
        this.stepCount = stepCount;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public Integer getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }
}
