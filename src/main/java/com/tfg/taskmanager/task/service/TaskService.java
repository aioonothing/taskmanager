package com.tfg.taskmanager.task.service;

import com.tfg.taskmanager.task.model.dto.TaskCreateDTO;
import com.tfg.taskmanager.task.model.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    /**
     * Crea una nueva tarea para un proyecto.
     * @param dto datos de entrada
     * @param creator usuario autenticado (para trazabilidad o validación futura)
     * @return tarea creada
     */
    TaskDTO createTask(TaskCreateDTO dto, String creator);

    /**
     * Obtiene todas las tareas de un proyecto específico.
     */
    List<TaskDTO> getTasksByProject(Long projectId);

    /**
     * Recupera una tarea por su ID.
     */
    TaskDTO getTask(Long taskId);
}
