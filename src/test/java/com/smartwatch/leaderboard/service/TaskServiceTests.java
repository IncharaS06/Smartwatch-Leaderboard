package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.TaskDto;
import com.smartwatch.leaderboard.model.Task;
import com.smartwatch.leaderboard.repository.TaskRepository;
import com.smartwatch.leaderboard.repository.UserTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTests {

    private TaskRepository taskRepository;
    private UserTaskRepository userTaskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        userTaskRepository = mock(UserTaskRepository.class);
        taskService = new TaskService(taskRepository, userTaskRepository);
    }

    @Test
    void testCreateTask() {
        TaskDto dto = new TaskDto(null, "Task Title", "Desc", 100, Set.of("GPS"));
        Task saved = new Task(1L, "Task Title", "Desc", 100, Set.of("GPS"));

        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        TaskDto result = taskService.createTask(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Task Title", result.getTitle());
        assertEquals(100, result.getPoints());
    }

    @Test
    void testGetAllTasks() {
        Task task = new Task(1L, "Task Title", "Desc", 100, Set.of("GPS"));
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskDto> result = taskService.getAllTasks();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testGetTaskByIdSuccess() {
        Task task = new Task(1L, "Task Title", "Desc", 100, Set.of("GPS"));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDto result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetTaskByIdThrowsExceptionIfNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void testUpdateTask() {
        Task existing = new Task(1L, "Old Title", "Desc", 100, Set.of("GPS"));
        TaskDto dto = new TaskDto(1L, "New Title", "Desc", 150, Set.of("GPS"));
        Task updated = new Task(1L, "New Title", "Desc", 150, Set.of("GPS"));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenReturn(updated);

        TaskDto result = taskService.updateTask(1L, dto);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals(150, result.getPoints());
    }
}
