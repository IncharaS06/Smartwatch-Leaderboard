package com.smartwatch.leaderboard.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer points;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "task_required_tags", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "required_tag")
    private Set<String> requiredFeatureTags = new HashSet<>();

    public Task() {
    }

    public Task(Long id, String title, String description, Integer points, Set<String> requiredFeatureTags) {
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

    // Manual Builder
    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public static class TaskBuilder {
        private Long id;
        private String title;
        private String description;
        private Integer points;
        private Set<String> requiredFeatureTags = new HashSet<>();

        public TaskBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TaskBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TaskBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TaskBuilder points(Integer points) {
            this.points = points;
            return this;
        }

        public TaskBuilder requiredFeatureTags(Set<String> requiredFeatureTags) {
            this.requiredFeatureTags = requiredFeatureTags;
            return this;
        }

        public Task build() {
            return new Task(id, title, description, points, requiredFeatureTags);
        }
    }
}
