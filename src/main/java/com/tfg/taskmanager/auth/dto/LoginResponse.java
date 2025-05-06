package com.tfg.taskmanager.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para el login de un usuario.
 * Contiene el token de acceso, el nombre de usuario y el email del usuario.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoginResponse {
    private String token;
    private String username;
    private String email;
}
