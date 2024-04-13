package com.dilmurod.taskmanager.service;

import com.dilmurod.taskmanager.model.Task;
import com.dilmurod.taskmanager.model.User;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface TaskService {
    Task create(Task task);
    Task update(Long id, Task task);
    Task getById(Long id);
    List<Task> getAll(User user) throws AccessDeniedException;
    void delete(Long id);
}

