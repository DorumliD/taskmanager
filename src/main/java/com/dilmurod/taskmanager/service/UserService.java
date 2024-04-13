package com.dilmurod.taskmanager.service;

import com.dilmurod.taskmanager.model.User;

public interface UserService {
    User register(User user);
    void authenticate(String username, String password);
    void logout();
}
