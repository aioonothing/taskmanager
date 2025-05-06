package com.tfg.taskmanager.task.repository;

import com.tfg.taskmanager.task.model.vo.Task;
import com.tfg.taskmanager.task.model.vo.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de acceso a datos para tareas.
 * Permite operaciones CRUD y consultas por campos específicos.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Obtiene todas las tareas asociadas a un proyecto.
     */
    List<Task> findByProjectId(Long projectId);

    /**
     * (Opcional) Filtra tareas por usuario asignado.
     */
    List<Task> findByAssignedTo(String username);

    /**
     * (Opcional) Tareas por estado.
     */
    List<Task> findByStatus(TaskStatus status);
}
