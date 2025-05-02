package com.tfg.taskmanager.project.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;

    private LocalDateTime createdAt;

    private LocalDate startDate;

    private LocalDate estimatedEndDate;

    private LocalDate realEndDate;

    private Integer estimatedEffortHours;

    private Integer actualEffortHours;

    private Integer participantsCount;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private String ownerUsername;

    @Column(length = 4000)
    private String viabilitySummary;

    // Inicializa automáticamente la fecha de creación
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
