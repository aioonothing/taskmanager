package com.tfg.taskmanager.project.controller;

import com.tfg.taskmanager.project.model.dto.ProjectCreateDTO;
import com.tfg.taskmanager.project.model.dto.ProjectDTO;
import com.tfg.taskmanager.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar proyectos.
 * Requiere autenticación mediante JWT.
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    /**
     * Crea un nuevo proyecto con los datos proporcionados.
     * @param dto datos del proyecto
     * @param auth contexto de autenticación (extrae el usuario del JWT)
     * @return el proyecto creado
     */
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
            @Valid @RequestBody ProjectCreateDTO dto,
            Authentication auth) {

        String username = auth.getName(); // Extraído del token JWT
        ProjectDTO created = service.createProject(dto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Lista todos los proyectos pertenecientes al usuario autenticado.
     * @param auth contexto con datos del usuario
     * @return lista de proyectos
     */
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(Authentication auth) {
        String username = auth.getName();
        List<ProjectDTO> projects = service.getAllByOwner(username);
        return ResponseEntity.ok(projects);
    }

    /**
     * Recupera un proyecto específico por ID.
     * @param id identificador del proyecto
     * @return el proyecto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = service.getProject(id);
        return ResponseEntity.ok(project);
    }
}
