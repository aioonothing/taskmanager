package com.tfg.taskmanager.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para el login de un usuario.
 * Contiene el email y la contraseña del usuario.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoginRequest {

    private String username;
    private String password;
}
