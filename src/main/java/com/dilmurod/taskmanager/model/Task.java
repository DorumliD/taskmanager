package com.dilmurod.taskmanager.model;

import java.util.Date;
import com.dilmurod.taskmanager.util.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private Date dueDate;

    @Enumerated(EnumType.STRING) // Помечаем, что это поле использует перечисление
    @Column(name = "priority")
    private Priority priority; // Используем перечисление Priority для поля priority

    @Column(name = "completed")
    private boolean completed;
}

