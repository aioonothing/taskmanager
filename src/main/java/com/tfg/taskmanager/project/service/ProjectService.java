package com.tfg.taskmanager.project.service;

import com.tfg.taskmanager.project.model.dto.ProjectCreateDTO;
import com.tfg.taskmanager.project.model.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {

    /**
     * Crea un nuevo proyecto para el usuario autenticado.
     * @param dto DTO con los datos del proyecto
     * @param ownerUsername nombre del usuario extraído del token
     * @return Proyecto creado
     */
    ProjectDTO createProject(ProjectCreateDTO dto, String ownerUsername);

    /**
     * Recupera todos los proyectos creados por un usuario específico.
     * @param ownerUsername usuario autenticado
     * @return lista de proyectos
     */
    List<ProjectDTO> getAllByOwner(String ownerUsername);

    /**
     * Recupera un proyecto por ID.
     * @param id identificador del proyecto
     * @return el DTO del proyecto
     */
    ProjectDTO getProject(Long id);
}
