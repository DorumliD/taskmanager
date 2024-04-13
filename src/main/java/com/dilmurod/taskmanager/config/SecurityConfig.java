package com.dilmurod.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean // Объявляем бин PasswordEncoder
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/users/register", "/api/users/login").permitAll() // Разрешаем доступ к странице регистрации всем
                .antMatchers("/api/tasks").authenticated() // Разрешаем доступ к /api/tasks только аутентифицированным пользователям
                .anyRequest().authenticated() // Для всех остальных запросов требуем аутентификацию
                .and()
                .logout() // Включаем поддержку выхода из системы
                .logoutUrl("/api/users/logout") // Указываем URL для выхода
                .permitAll(); // Разрешаем доступ к странице выхода всем
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Настройка пользователей для аутентификации
        auth
                .inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER"); // Простой пользователь для примера
    }
}

