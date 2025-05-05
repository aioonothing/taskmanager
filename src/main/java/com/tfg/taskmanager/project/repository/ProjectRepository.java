package com.tfg.taskmanager.project.repository;

import com.tfg.taskmanager.project.model.vo.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad Project.
 * Proporciona operaciones CRUD y consultas adicionales por nombre de método.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Recupera todos los proyectos creados por un usuario específico.
     * Ideal para mostrar solo los proyectos del propietario.
     */
    List<Project> findByOwnerUsername(String ownerUsername);

    /**
     * Consulta opcional: obtener proyectos por estado.
     */
    List<Project> findByStatus(com.tfg.taskmanager.project.model.vo.ProjectStatus status);
}
