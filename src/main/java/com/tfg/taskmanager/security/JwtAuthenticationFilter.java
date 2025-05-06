package com.tfg.taskmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT que se ejecuta en cada petición HTTP.
 * Valida el token, extrae el usuario y lo establece en el contexto de seguridad.
 */
@Slf4j // Registra eventos y errores en logs, útil para depuración.
@Component // Permite la inyección automática en Spring Boot.
@RequiredArgsConstructor // Genera un constructor con los atributos final (inyección de dependencias automática).
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Garantiza que el filtro se ejecute solo una vez por solicitud.

    private final JwtService jwtService; // Servicio que gestiona validación y extracción de datos del JWT.

    /**
     * Intercepta la solicitud HTTP y valida el token de autenticación.
     * @param request Petición HTTP que llega al servidor.
     * @param response Respuesta HTTP que el servidor enviará al cliente.
     * @param filterChain Cadena de filtros que continúa el procesamiento normal.
     * @throws ServletException Si hay un error interno en el filtro.
     * @throws IOException Si hay un error en la comunicación HTTP.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Validación inicial del header de autenticación.
        // Un JWT válido siempre comienza con "Bearer ", seguido del token real.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Si no hay token, se permite continuar sin autenticación.
            return;
        }

        final String jwt = authHeader.substring(7); // Extrae solo el token eliminando "Bearer ".
        final String username = jwtService.extractUsername(jwt); // Obtiene el nombre de usuario desde el JWT.

        // Si el usuario ya está autenticado en Spring Security, no repetimos el proceso.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Validamos que el token sea legítimo.
            if (jwtService.isTokenValid(jwt)) {

                // Se crea un objeto de autenticación para establecer el usuario autenticado en el contexto de seguridad.
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null, // No se usa credencial adicional porque el JWT ya verifica identidad.
                                null  // Roles no procesados aquí, pero pueden incluirse más adelante si es necesario.
                        );

                // Asigna detalles adicionales (IP, sesión, etc.) a la autenticación.
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Establece la autenticación en el contexto de seguridad de Spring.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continúa con la cadena de filtros para que la solicitud llegue al controlador correspondiente.
        filterChain.doFilter(request, response);
    }
}
