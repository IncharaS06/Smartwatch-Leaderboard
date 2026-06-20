package com.smartwatch.leaderboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_tasks")
public class UserTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "step_count")
    private Integer stepCount;

    @Column(name = "completion_date", nullable = false)
    private LocalDateTime completionDate;

    @Column(name = "points_earned", nullable = false)
    private Integer pointsEarned;

    public UserTask() {
    }

    public UserTask(Long id, User user, Task task, Integer stepCount, LocalDateTime completionDate, Integer pointsEarned) {
        this.id = id;
        this.user = user;
        this.task = task;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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

    // Manual Builder
    public static UserTaskBuilder builder() {
        return new UserTaskBuilder();
    }

    public static class UserTaskBuilder {
        private Long id;
        private User user;
        private Task task;
        private Integer stepCount;
        private LocalDateTime completionDate;
        private Integer pointsEarned;

        public UserTaskBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserTaskBuilder user(User user) {
            this.user = user;
            return this;
        }

        public UserTaskBuilder task(Task task) {
            this.task = task;
            return this;
        }

        public UserTaskBuilder stepCount(Integer stepCount) {
            this.stepCount = stepCount;
            return this;
        }

        public UserTaskBuilder completionDate(LocalDateTime completionDate) {
            this.completionDate = completionDate;
            return this;
        }

        public UserTaskBuilder pointsEarned(Integer pointsEarned) {
            this.pointsEarned = pointsEarned;
            return this;
        }

        public UserTask build() {
            return new UserTask(id, user, task, stepCount, completionDate, pointsEarned);
        }
    }
}
