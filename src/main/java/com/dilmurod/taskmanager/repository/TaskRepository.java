package com.dilmurod.taskmanager.repository;

import com.dilmurod.taskmanager.model.Task;
import com.dilmurod.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
}
