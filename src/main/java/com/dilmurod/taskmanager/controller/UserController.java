package com.dilmurod.taskmanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dilmurod.taskmanager.model.User;
import com.dilmurod.taskmanager.service.UserServiceImpl;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser( @RequestBody User user, BindingResult bindingResult) {
        // Проверяем наличие ошибок валидации
        if (bindingResult.hasErrors()) {
            // Создаем строку для хранения сообщений об ошибках
            StringBuilder errorMessage = new StringBuilder();
            // Обходим все ошибки валидации
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessage.append(error.getDefaultMessage()).append(". ");
            }
            // Возвращаем ошибку валидации с сообщениями об ошибках
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        try {
            // Регистрируем пользователя
            User registeredUser = userServiceImpl.register(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (Exception e) {
            // В случае возникновения исключения возвращаем ошибку сервера
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка регистрации: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public void logout() {
        userServiceImpl.logout();
    }
    // Можно добавить другие методы контроллера для аутентификации и выхода из системы

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody User user) {
        try {
            logger.info("Attempting to authenticate user: {}", user.getUsername());
            // Пытаемся аутентифицировать пользователя с заданными данными
            userServiceImpl.authenticate(user.getUsername(), user.getPassword());
            logger.info("User {} successfully authenticated", user.getUsername());
            // Возвращаем успешный ответ
            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for user {}: Invalid credentials", user.getUsername());
            // Если аутентификация не удалась из-за неправильных учетных данных
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка аутентификации: Неверные учетные данные");
        } catch (LockedException e) {
            logger.error("Authentication failed for user {}: Account locked", user.getUsername());
            // Если учетная запись заблокирована
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка аутентификации: Учетная запись заблокирована");
        } catch (DisabledException e) {
            logger.error("Authentication failed for user {}: Account disabled", user.getUsername());
            // Если учетная запись отключена
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка аутентификации: Учетная запись отключена");
        } catch (Exception e) {
            logger.error("Authentication failed for user {}: {}", user.getUsername(), e.getMessage());
            // В случае других исключений возвращаем общее сообщение об ошибке
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка аутентификации: " + e.getMessage());
        }
    }



}

