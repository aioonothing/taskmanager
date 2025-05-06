package com.tfg.taskmanager.task.service;

import com.tfg.taskmanager.task.model.dto.TaskCreateDTO;
import com.tfg.taskmanager.task.model.dto.TaskDTO;
import com.tfg.taskmanager.task.mapper.TaskMapper;
import com.tfg.taskmanager.task.model.vo.Task;
import com.tfg.taskmanager.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de tareas.
 * 
 * Implementación de `TaskService` que encapsula la lógica de negocio
 * para la creación y recuperación de tareas dentro del sistema.
 * 
 * Este servicio interactúa con:
 * - `TaskRepository`: Para acceder a la base de datos de tareas.
 * - `TaskMapper`: Para la conversión entre entidades y DTOs.
 * 
 * Se usa `@Service` para marcar la clase como un componente de negocio en Spring.
 * Se usa `@RequiredArgsConstructor` para la inyección de dependencias sin necesidad de un constructor manual.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    /** Repositorio para operaciones CRUD sobre la entidad Task */
    private final TaskRepository repository;
    
    /** Mapper encargado de convertir entre DTOs y entidades */
    private final TaskMapper mapper;

    /**
     * Crea una nueva tarea en la base de datos.
     * 
     * @param dto Objeto DTO con la información de la tarea a crear.
     * @param creator Nombre de usuario del creador de la tarea.
     * @return DTO de la tarea creada.
     */
    @Override
    public TaskDTO createTask(TaskCreateDTO dto, String creator) {

        // Convierte el DTO recibido en una entidad persistente
        Task task = mapper.toEntity(dto);
        
        // (Opcional) Se puede almacenar el creador para trazabilidad futura
        // task.setCreator(creator);
        
        // Guarda la tarea en la base de datos
        Task saved = repository.save(task);
        
        // Convierte la entidad guardada en DTO para respuesta
        return mapper.toDTO(saved);
    }

    /**
     * Obtiene todas las tareas asociadas a un proyecto específico.
     * 
     * @param projectId ID único del proyecto al que pertenecen las tareas.
     * @return Lista de tareas en formato DTO.
     */
    @Override
    public List<TaskDTO> getTasksByProject(Long projectId) {

        // Consulta las tareas en la base de datos filtrando por proyecto
        return repository.findByProjectId(projectId)
                .stream() // Convierte la lista obtenida en un Stream para transformación
                .map(mapper::toDTO) // Convierte cada entidad Task en su DTO correspondiente
                .collect(Collectors.toList()); // Convierte el Stream en una lista final
    }

    /**
     * Obtiene una tarea específica por su identificador.
     * 
     * @param taskId ID único de la tarea.
     * @return DTO de la tarea si existe.
     * @throws RuntimeException Si no se encuentra la tarea.
     */
    @Override
    public TaskDTO getTask(Long taskId) {

        // Busca la tarea por su ID, si no existe lanza una excepción controlada
        Task task = repository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // Retorna la tarea en formato DTO
        return mapper.toDTO(task);
    }
}
