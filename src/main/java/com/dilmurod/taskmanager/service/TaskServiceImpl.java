package com.dilmurod.taskmanager.service;

import com.dilmurod.taskmanager.model.Task;
import com.dilmurod.taskmanager.model.User;
import com.dilmurod.taskmanager.repository.TaskRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task update(Long id, Task task) {
        task.setId(id);
        return taskRepository.save(task);
    }

    @Override
    public Task getById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public List<Task> getAll(User user) throws AccessDeniedException {
        // Проверяем, что пользователь аутентифицирован
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            // Получаем имя аутентифицированного пользователя
            String currentUserName = authentication.getName();
            // Проверяем, что имя аутентифицированного пользователя совпадает с именем пользователя, для которого запрашиваются задачи
            if (currentUserName.equals(user.getUsername())) {
                // Возвращаем задачи для аутентифицированного пользователя
                return taskRepository.findByUser(user);
            } else {
                // Если запрошены задачи для другого пользователя, выбрасываем исключение или возвращаем пустой список
                throw new AccessDeniedException("Access is denied");
                // Или вернуть пустой список: return Collections.emptyList();
            }
        } else {
            // Если пользователь не аутентифицирован, выбрасываем исключение или возвращаем пустой список
            throw new AccessDeniedException("Access is denied");
            // Или вернуть пустой список: return Collections.emptyList();
        }
    }


    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}

