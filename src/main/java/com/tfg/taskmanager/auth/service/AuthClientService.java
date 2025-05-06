package com.tfg.taskmanager.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.tfg.taskmanager.auth.dto.LoginRequest;
import com.tfg.taskmanager.auth.dto.LoginResponse;
import com.tfg.taskmanager.auth.dto.RegisterRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthClientService {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final RestTemplate restTemplate;

    /**
     * Realiza login contra el auth-service.
     * @param request DTO con email y contraseña
     * @return respuesta con token y datos del usuario
     */
    public LoginResponse login(LoginRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<LoginResponse> response = restTemplate.exchange(
                    authServiceUrl, // Url del auth-service descrita en porperties
                    HttpMethod.POST,
                    entity,
                    LoginResponse.class
            );

            return response.getBody();

        } catch (HttpClientErrorException e) {
            // 4xx: credenciales inválidas u otro error del cliente
            log.warn("Error de autenticación desde auth-service: {}", e.getMessage());
            throw new RuntimeException("Credenciales incorrectas");

        } catch (ResourceAccessException e) {
            // Fallo de red o servidor inaccesible
            log.error("No se puede acceder al auth-service: {}", e.getMessage());
            throw new RuntimeException("Error de conexión con auth-service");

        } catch (Exception e) {
            log.error("Error inesperado en login: {}", e.getMessage());
            throw new RuntimeException("Error inesperado al autenticar");
        }
    }

    /**
 * Llama al microservicio de autenticación para registrar un nuevo usuario.
 * @param request datos del nuevo usuario (username, email, password)
 * @throws RuntimeException si ocurre un error durante el registro
 */
    public void register(RegisterRequest request) {
        try {
            String url = authServiceUrl  + "/auth/register"; // Ajusta si la URL cambia
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }

}
