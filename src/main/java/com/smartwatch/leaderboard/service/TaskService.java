package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.TaskDto;
import com.smartwatch.leaderboard.dto.UserTaskResponseDto;
import com.smartwatch.leaderboard.model.Task;
import com.smartwatch.leaderboard.model.UserTask;
import com.smartwatch.leaderboard.repository.TaskRepository;
import com.smartwatch.leaderboard.repository.UserTaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserTaskRepository userTaskRepository;

    public TaskService(TaskRepository taskRepository, UserTaskRepository userTaskRepository) {
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
    }

    @Transactional
    public TaskDto createTask(TaskDto dto) {
        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .points(dto.getPoints())
                .requiredFeatureTags(dto.getRequiredFeatureTags())
                .build();
        Task saved = taskRepository.save(task);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        return mapToDto(task);
    }

    @Transactional
    public TaskDto updateTask(Long id, TaskDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPoints(dto.getPoints());
        task.setRequiredFeatureTags(dto.getRequiredFeatureTags());

        Task saved = taskRepository.save(task);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public Page<UserTaskResponseDto> getPaginatedTaskUsers(Long taskId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserTask> userTasks = userTaskRepository.findByTaskId(taskId, pageable);
        return userTasks.map(this::mapToUserTaskResponseDto);
    }

    private TaskDto mapToDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPoints(),
                task.getRequiredFeatureTags()
        );
    }

    private UserTaskResponseDto mapToUserTaskResponseDto(UserTask ut) {
        return new UserTaskResponseDto(
                ut.getId(),
                ut.getUser().getId(),
                ut.getUser().getFullName(),
                ut.getTask().getId(),
                ut.getTask().getTitle(),
                ut.getStepCount(),
                ut.getCompletionDate(),
                ut.getPointsEarned()
        );
    }
}
