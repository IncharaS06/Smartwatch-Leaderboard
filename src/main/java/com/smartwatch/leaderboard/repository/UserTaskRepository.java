package com.smartwatch.leaderboard.repository;

import com.smartwatch.leaderboard.model.UserTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    Page<UserTask> findByTaskId(Long taskId, Pageable pageable);
}
