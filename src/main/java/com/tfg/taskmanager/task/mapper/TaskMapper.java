package com.tfg.taskmanager.task.mapper;

import com.tfg.taskmanager.task.model.dto.TaskCreateDTO;
import com.tfg.taskmanager.task.model.dto.TaskDTO;
import com.tfg.taskmanager.task.model.vo.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    /**
     * Convierte una entidad Task a un DTO de salida.
     */
    TaskDTO toDTO(Task task);

    /**
     * Convierte un DTO de creación a entidad Task.
     * Se ignoran campos generados automáticamente por el sistema.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Task toEntity(TaskCreateDTO dto);
}
