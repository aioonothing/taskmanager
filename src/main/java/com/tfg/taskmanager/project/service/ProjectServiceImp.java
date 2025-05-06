package com.tfg.taskmanager.project.service;

import com.tfg.taskmanager.project.mapper.ProjectMapper;
import com.tfg.taskmanager.project.model.dto.ProjectCreateDTO;
import com.tfg.taskmanager.project.model.dto.ProjectDTO;
import com.tfg.taskmanager.project.model.vo.Project;
import com.tfg.taskmanager.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio responsable de la gestión de proyectos dentro del sistema.
 * Implementa la lógica de negocio y proporciona métodos para crear, obtener y listar proyectos.
 * 
 * Este servicio interactúa con:
 * - `ProjectRepository`: Para acceder a la base de datos.
 * - `ProjectMapper`: Para la conversión entre entidades y DTOs.
 * 
 * Se usa `@Service` para marcar la clase como un componente de negocio en Spring.
 * Se usa `@RequiredArgsConstructor` para la inyección de dependencias sin necesidad de un constructor manual.
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImp implements ProjectService {

    /** Repositorio para operaciones CRUD sobre la entidad Project */
    private final ProjectRepository repository;
    
    /** Mapper encargado de convertir entre DTOs y entidades */
    private final ProjectMapper mapper;

    /**
     * Crea un nuevo proyecto en la base de datos.
     * 
     * @param dto Objeto DTO con la información del proyecto a crear.
     * @param ownerUsername Nombre de usuario del propietario (extraído del JWT).
     * @return DTO del proyecto creado.
     */
    @Override
    public ProjectDTO createProject(ProjectCreateDTO dto, String ownerUsername) {

        // Convierte el DTO recibido en una entidad de base de datos
        Project project = mapper.toEntity(dto);
        
        // Asigna el usuario propietario al proyecto (según autenticación)
        project.setOwnerUsername(ownerUsername);
        
        // Guarda el proyecto en la base de datos
        Project saved = repository.save(project);
        
        // Convierte la entidad persistida en DTO para su devolución
        return mapper.toDTO(saved);
    }

    /**
     * Obtiene todos los proyectos creados por un usuario específico.
     * 
     * @param ownerUsername Nombre de usuario del propietario.
     * @return Lista de proyectos en formato DTO.
     */
    @Override
    public List<ProjectDTO> getAllByOwner(String ownerUsername) {

        // Consulta los proyectos en la base de datos filtrando por propietario
        return repository.findByOwnerUsername(ownerUsername)
                .stream() // Convierte la lista obtenida en un Stream para transformación
                .map(mapper::toDTO) // Convierte cada entidad Project en su DTO correspondiente
                .collect(Collectors.toList()); // Convierte el Stream en una lista final
    }

    /**
     * Obtiene un proyecto específico por su identificador.
     * 
     * @param id ID único del proyecto.
     * @return DTO del proyecto si existe.
     * @throws RuntimeException Si no se encuentra el proyecto.
     */
    @Override
    public ProjectDTO getProject(Long id) {

        // Busca el proyecto por su ID, si no existe lanza una excepción controlada
        Project project = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        // Retorna el proyecto en formato DTO
        return mapper.toDTO(project);
    }
}
