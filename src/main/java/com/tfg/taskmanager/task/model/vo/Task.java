package com.tfg.taskmanager.task.model.vo;

import jakarta.persistence.*;
import lombok.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa una tarea dentro de un proyecto.
 * Diseñada para reflejar su estado, prioridad, responsable y organización visual.
 */
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Task {

    /**
     * Identificador único de la tarea.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título descriptivo y corto de la tarea.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Descripción extendida con detalles técnicos o funcionales.
     */
    @Column(length = 2000)
    private String description;

    /**
     * ID del proyecto al que pertenece la tarea.
     */
    private Long projectId;

    /**
     * Usuario asignado a la tarea (por nombre de usuario del sistema).
     */
    private String assignedTo;

    /**
     * Estado actual de la tarea: TODO, IN_PROGRESS, DONE, BLOCKED.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    /**
     * Nivel de prioridad: LOW, MEDIUM, HIGH, CRITICAL.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    /**
     * Fecha límite para completar la tarea.
     */
    private LocalDate dueDate;

    /**
     * Fecha de creación automática.
     */
    private LocalDateTime createdAt;

    /**
     * Posición visual dentro de la columna (opcional).
     */
    private Integer position;

    /**
     * Lista de etiquetas asociadas a la tarea.
     */
    @ElementCollection
    private List<String> tags;

    /**
     * Asigna fecha de creación automáticamente al persistir.
     */
    @PrePersist
    public void setCreationTimestamp() {
        this.createdAt = LocalDateTime.now();
    }
}
