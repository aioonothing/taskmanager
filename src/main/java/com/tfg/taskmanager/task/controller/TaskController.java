package com.tfg.taskmanager.task.controller;

import com.tfg.taskmanager.task.model.dto.TaskCreateDTO;
import com.tfg.taskmanager.task.model.dto.TaskDTO;
import com.tfg.taskmanager.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de tareas dentro del sistema.
 * 
 * Expone endpoints para la creación y consulta de tareas, asegurando una
 * integración con autenticación JWT para la identificación del usuario.
 * 
 * Principios seguidos:
 * - **Responsabilidad Única:** Solo maneja solicitudes HTTP relacionadas con tareas.
 * - **Modularidad:** Se integra con `TaskService`, evitando lógica de negocio en el controlador.
 * - **Seguridad:** Utiliza `Authentication` para garantizar que las operaciones sean realizadas por usuarios autenticados.
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    /** Servicio encargado de la lógica de negocio de tareas */
    private final TaskService taskService;

    /**
     * Crea una nueva tarea en el sistema.
     * 
     * @param dto Datos de la tarea recibidos desde el cuerpo de la solicitud.
     * @param auth Información del usuario autenticado (extraído desde el JWT).
     * @return Respuesta con la tarea creada y el estado HTTP 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @Valid @RequestBody TaskCreateDTO dto,
            Authentication auth) {

        // Extrae el nombre del usuario autenticado desde el token JWT
        String creator = auth.getName();
        
        // Delegamos la creación de la tarea a la capa de servicio
        TaskDTO task = taskService.createTask(dto, creator);
        
        // Retorna la tarea creada con estado 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    /**
     * Obtiene una tarea específica por su identificador.
     * 
     * @param id Identificador único de la tarea.
     * @return Respuesta con la tarea encontrada o estado 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {

        // Consulta la tarea en la base de datos
        TaskDTO task = taskService.getTask(id);

        // Retorna la tarea con estado 200 (OK)
        return ResponseEntity.ok(task);
    }

    /**
     * Lista todas las tareas asociadas a un proyecto.
     * 
     * @param projectId Identificador único del proyecto.
     * @return Lista de tareas relacionadas con el proyecto solicitado.
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskDTO>> getTasksByProject(@PathVariable Long projectId) {

        // Obtiene todas las tareas asociadas al proyecto
        List<TaskDTO> tasks = taskService.getTasksByProject(projectId);

        // Retorna la lista de tareas con estado 200 (OK)
        return ResponseEntity.ok(tasks);
    }
}
