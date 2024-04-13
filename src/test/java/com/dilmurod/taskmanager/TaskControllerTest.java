package com.dilmurod.taskmanager;

import com.dilmurod.taskmanager.controller.TaskController;
import com.dilmurod.taskmanager.model.Task;
import com.dilmurod.taskmanager.model.User;
import com.dilmurod.taskmanager.service.TaskServiceImpl;
import com.dilmurod.taskmanager.util.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskServiceImpl taskService;

    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskServiceImpl.class);
        taskController = new TaskController(taskService);
    }

    @Test
    void createTaskTest() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Description for Test Task");

        when(taskService.create(task)).thenReturn(task);

        ResponseEntity<Task> responseEntity = taskController.createTask(task);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(task, responseEntity.getBody());
        verify(taskService, times(1)).create(task);
    }

    @Test
    void getTaskByIdTest() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        task.setDescription("Description for Test Task");

        when(taskService.getById(taskId)).thenReturn(task);

        ResponseEntity<Task> responseEntity = taskController.getTaskById(taskId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(task, responseEntity.getBody());
        verify(taskService, times(1)).getById(taskId);
    }

    @Test
    void getAllTasksTest() throws AccessDeniedException {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task(1L, null, "Task 1", "Description for Task 1", new Date(), Priority.MEDIUM, false);
        Task task2 = new Task(2L, null, "Task 2", "Description for Task 2", new Date(), Priority.LOW, true);
        tasks.add(task1);
        tasks.add(task2);

        when(taskService.getAll(null)).thenReturn(tasks);

        ResponseEntity<List<Task>> responseEntity = taskController.getAllTasks(null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tasks, responseEntity.getBody());
        verify(taskService, times(1)).getAll(null);
    }

    @Test
    void updateTaskTest() {
        Long taskId = 1L;
        Task updatedTask = new Task(taskId, null, "Updated Task", "Updated Description", new Date(), Priority.HIGH, true);

        when(taskService.update(taskId, updatedTask)).thenReturn(updatedTask);

        ResponseEntity<Task> responseEntity = taskController.updateTask(taskId, updatedTask);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedTask, responseEntity.getBody());
        verify(taskService, times(1)).update(taskId, updatedTask);
    }

    @Test
    void deleteTaskTest() {
        Long taskId = 1L;

        ResponseEntity<Void> responseEntity = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(taskService, times(1)).delete(taskId);
    }
}
