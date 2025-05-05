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
 * Implementación de la interfaz ProjectService.
 * Contiene la lógica de negocio relacionada con la gestión de proyectos.
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImp implements ProjectService{

    private final ProjectRepository repository;
    private final ProjectMapper mapper;

    @Override
    public ProjectDTO createProject(ProjectCreateDTO dto, String ownerUsername) {

        // Mapeo del DTO a entidad
        Project project = mapper.toEntity(dto);
        // Asignación del propietario (extraído del JWT)
        project.setOwnerUsername(ownerUsername);
        // Guardado en base de datos
        Project saved = repository.save(project);
        // Devolución del DTO
        return mapper.toDTO(saved);
    }

    @Override
    public List<ProjectDTO> getAllByOwner(String ownerUsername) {

        return repository.findByOwnerUsername(ownerUsername)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProject(Long id) {

        Project project = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        return mapper.toDTO(project);
    }
}
