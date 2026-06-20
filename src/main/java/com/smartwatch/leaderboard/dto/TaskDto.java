package com.smartwatch.leaderboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public class TaskDto {
    private Long id;

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    @NotNull(message = "Points value is required")
    private Integer points;

    private Set<String> requiredFeatureTags;

    public TaskDto() {
    }

    public TaskDto(Long id, String title, String description, Integer points, Set<String> requiredFeatureTags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.points = points;
        this.requiredFeatureTags = requiredFeatureTags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Set<String> getRequiredFeatureTags() {
        return requiredFeatureTags;
    }

    public void setRequiredFeatureTags(Set<String> requiredFeatureTags) {
        this.requiredFeatureTags = requiredFeatureTags;
    }
}
