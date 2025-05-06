package com.tfg.taskmanager.task.model.dto;

import com.tfg.taskmanager.task.model.vo.TaskPriority;
import com.tfg.taskmanager.task.model.vo.TaskStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de salida que representa una tarea con todos sus detalles.
 * Se utiliza en las respuestas del API REST.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {

    private Long id;
    private String title;
    private String description;

    private Long projectId;
    private String assignedTo;

    private TaskStatus status;
    private TaskPriority priority;

    private LocalDate dueDate;
    private LocalDateTime createdAt;

    private Integer position;
    private List<String> tags;
}
