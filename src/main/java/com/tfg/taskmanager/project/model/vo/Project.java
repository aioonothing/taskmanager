package com.tfg.taskmanager.project.model.vo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Entidad que representa un proyecto dentro del sistema.
 * Es la unidad principal de organización para tareas y equipos.
 */
@Entity
@Table(name= "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id // Identificador unico de proyecto generado auto.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    /**
     * Fecha y hora en que se creó el proyecto.
     * Se genera automáticamente al persistir.
     */
    private LocalDateTime createdAt;

    /**
     * Fecha de inicio del proyecto, según planificación.
     */
    private LocalDate startDate;

    /**
     * Fecha de finalización estimada.
     */
    private LocalDate estimatedEndDate;

    /**
     * Fecha real de finalización (puede ser null si aún está activo).
     */
    private LocalDate realEndDate;

    /**
     * Esfuerzo estimado en horas para completar el proyecto.
     */
    private Integer estimatedEffortHours;

    /**
     * Esfuerzo real invertido (relleno al finalizar).
     */
    private Integer actualEffortHours;

    /**
     * Número de personas asignadas al proyecto.
     * Este dato puede ser usado para análisis de viabilidad.
     */
    private Integer participantsCount;

    /**
     * Estado del proyecto: PLANNED, ACTIVE, COMPLETED, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    /**
     * Nombre del usuario que ha creado el proyecto.
     * Extraído del JWT y usado para trazabilidad.
     */
    private String ownerUsername;

    /**
     * Campo opcional que contendrá un resumen generado por IA
     * sobre la viabilidad del proyecto.
     */
    @Column(length = 4000)
    private String viabilitySummary;

    /**
     * ID del equipo asignado. Relación @ManyToOne con Team futura !!!!!!!!!.
     */
    private Long teamId;

    /**
     * Lista de etiquetas del proyecto.
     * Relación con entidad Tag futura !!!!!!!!!.
     */
    @ElementCollection
    private List<String> tags;

    // Inicializa automáticamente la fecha de creación
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
