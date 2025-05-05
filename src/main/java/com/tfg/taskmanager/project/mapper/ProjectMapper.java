package com.tfg.taskmanager.project.mapper;

import com.tfg.taskmanager.project.model.dto.ProjectCreateDTO;
import com.tfg.taskmanager.project.model.dto.ProjectDTO;
import com.tfg.taskmanager.project.model.vo.Project;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    /**
     * Convierte una entidad Project a un DTO para respuesta al cliente.
     * Se hace de manera natural con MapStruct debido a similitud de estructuras.
     */
    ProjectDTO toDTO(Project project);


    /**
     * Convierte un DTO de creación a una entidad Project.
     * Algunos campos son ignorados porque se asignan desde la lógica de negocio.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "realEndDate", ignore = true)
    @Mapping(target = "actualEffortHours", ignore = true)
    @Mapping(target = "status", constant = "PLANNED")
    @Mapping(target = "ownerUsername", ignore = true)
    @Mapping(target = "viabilitySummary", ignore = true)
    @Mapping(target = "riskScore", ignore = true)
    @Mapping(target = "viabilityCategory", ignore = true)
    @Mapping(target = "isViable", ignore = true)

    Project toEntity(ProjectCreateDTO dto);
}
