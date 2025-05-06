package com.tfg.taskmanager.task.model.dto;

import com.tfg.taskmanager.task.model.vo.TaskPriority;
import com.tfg.taskmanager.task.model.vo.TaskStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO que representa los datos requeridos para crear una nueva tarea.
 * No incluye campos generados por el sistema (id, createdAt, etc.).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCreateDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no debe superar los 100 caracteres")
    private String title;

    @Size(max = 2000, message = "La descripción es demasiado larga")
    private String description;

    @NotNull(message = "Debes indicar el ID del proyecto al que pertenece")
    private Long projectId;

    private String assignedTo; // puede ser opcional

    @NotNull(message = "El estado de la tarea es obligatorio")
    private TaskStatus status;

    @NotNull(message = "La prioridad de la tarea es obligatoria")
    private TaskPriority priority;

    private LocalDate dueDate;

    private Integer position;

    private List<@Size(max = 20, message = "Cada etiqueta debe tener como máximo 20 caracteres") String> tags;
}