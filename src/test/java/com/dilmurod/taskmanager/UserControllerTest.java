package com.dilmurod.taskmanager;

import com.dilmurod.taskmanager.controller.UserController;
import com.dilmurod.taskmanager.model.User;
import com.dilmurod.taskmanager.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    void registerUserTest() {
        // Создаем пользователя для теста
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        // Создаем объект BindingResult для имитации результатов валидации
        BindingResult bindingResult = mock(BindingResult.class);

        // При имитации результатов валидации, не будут найдены ошибки
        when(bindingResult.hasErrors()).thenReturn(false);

        // Имитируем регистрацию пользователя и возвращаем его
        when(userService.register(user)).thenReturn(user);

        // Вызываем метод контроллера
        ResponseEntity<?> responseEntity = userController.registerUser(user, bindingResult);

        // Проверяем, что ответ содержит статус CREATED
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        // Проверяем, что возвращенный объект соответствует созданному пользователю
        assertEquals(user, responseEntity.getBody());

        // Проверяем, что метод регистрации пользователя был вызван один раз
        verify(userService, times(1)).register(user);
    }
    @Test
    void loginUserTest() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        ResponseEntity<Object> responseEntity = userController.loginUser(user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).authenticate("testUser", "testPassword");
    }

    @Test
    void logoutTest() {
        userController.logout();
        verify(userService, times(1)).logout();
    }
}
