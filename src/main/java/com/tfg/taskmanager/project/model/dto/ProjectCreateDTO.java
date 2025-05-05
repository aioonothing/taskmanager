package com.tfg.taskmanager.project.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

    @NotNull(message = "La fecha de inicio es obligatoria.")
    private LocalDate startDate;

    @NotNull(message = "La fecha estimada de finalización es obligatoria.")
    private LocalDate estimatedEndDate;

    @NotNull(message = "El esfuerzo estimado es obligatorio.")
    @Min(value = 1, message = "El esfuerzo debe ser al menos 1 hora.")
    private Integer estimatedEffortHours;

    @NotNull(message = "Debes indicar la cantidad de participantes.")
    @Min(value = 1, message = "Debe haber al menos 1 participante.")
    private Integer participantsCount;

    private List<@Size(max = 20, message = "Cada etiqueta debe tener como máximo 20 caracteres.") String> tags;
}
