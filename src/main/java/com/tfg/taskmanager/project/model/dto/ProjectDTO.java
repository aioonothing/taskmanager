package com.tfg.taskmanager.project.model.dto;

import com.tfg.taskmanager.project.model.vo.ProjectStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de salida que representa un proyecto ya creado en el sistema.
 * Expone todos los datos relevantes para visualización y análisis.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {

    private Long id;
    private String name;
    private String description;

    private LocalDateTime createdAt;
    private LocalDate startDate;
    private LocalDate estimatedEndDate;
    private LocalDate realEndDate;

    private Integer estimatedEffortHours;
    private Integer actualEffortHours;
    private Integer participantsCount;

    private ProjectStatus status;
    private String ownerUsername;

    private String viabilitySummary;
    private Integer riskScore;
    private String viabilityCategory;
    private Boolean isViable;

    private Long teamId;
    private List<String> tags;
}

