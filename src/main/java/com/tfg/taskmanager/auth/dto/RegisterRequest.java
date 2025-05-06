package com.tfg.taskmanager.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO utilizado para registrar nuevos usuarios desde la interfaz web.
 * Esta clase se utiliza tanto en el frontend (Thymeleaf) como en la comunicación con auth-service.
 *
 * Validaciones:
 * - Todos los campos son obligatorios.
 * - El email debe tener formato válido.
 * - La contraseña debe tener al menos 6 caracteres.
 */
@Data
public class RegisterRequest {

    /**
     * Nombre de usuario único que identificará al nuevo usuario.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    private String username;

    /**
     * Correo electrónico del usuario. Debe tener un formato válido.
     */
    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El email no tiene un formato válido.")
    private String email;

    /**
     * Contraseña que el usuario usará para autenticarse.
     */
    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;
}
