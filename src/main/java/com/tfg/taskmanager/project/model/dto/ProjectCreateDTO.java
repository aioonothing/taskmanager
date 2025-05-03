package com.tfg.taskmanager.project.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos mínimos que un usuario debe enviar para crear un nuevo proyecto
 * El servicio añadirá el resto (logica de servicio)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectCreateDTO {

    @NotBlank(message = "El nombre del proyecto es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String name;

    @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres.")
    private String description;
}
