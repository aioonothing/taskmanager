package com.tfg.taskmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
 * 🔐 Filtro de autenticación JWT que se ejecuta en cada petición HTTP.
 * 
 * Este filtro tiene la responsabilidad de:
 * - Extraer el JWT de la cabecera `Authorization` o de una cookie segura (`JWT_TOKEN`).
 * - Validar el token y extraer el nombre de usuario.
 * - Configurar el contexto de seguridad con el usuario autenticado si el token es válido.
 * 
 * ⚠️ **Consideraciones de seguridad**:
 * - Se admite autenticación mediante cabecera o cookie HttpOnly.
 * - La cookie `JWT_TOKEN` debe configurarse como segura en producción (`Secure=true`).
 * - No almacena roles directamente, permitiendo una validación externa si es necesaria.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** Servicio para validar y extraer información del JWT */
    private final JwtService jwtService;

    /**
     * 🔹 Método principal del filtro: intercepta cada petición y valida autenticación por JWT.
     * 
     * @param request Petición HTTP entrante.
     * @param response Respuesta HTTP saliente.
     * @param filterChain Cadena de filtros de Spring Security.
     * @throws ServletException Excepción de servlet en caso de error.
     * @throws IOException Excepción de I/O en caso de error.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Variables para almacenar el token y el username extraído
        String jwt = null;
        String username = null;

        // 1 Extraer JWT desde la cabecera Authorization
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Elimina "Bearer "
            username = jwtService.extractUsername(jwt);
        }

        // 2 Si el JWT no está en la cabecera, intentar recuperarlo desde la cookie JWT_TOKEN
        if ((jwt == null || username == null) && request.getCookies() != null) {
            jwt = extractTokenFromCookies(request);
            if (jwt != null) {
                username = jwtService.extractUsername(jwt);
            }
        }

        // 3 Validación del usuario y configuración del contexto de seguridad
        // Se verifica que haya un username y que aún no haya autenticación activa en el contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.isTokenValid(jwt)) { // Se valida la integridad y vigencia del JWT
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken); // Se establece el usuario autenticado
            }
        }

        // 4 Continúa la cadena de filtros para que la solicitud alcance su destino (controladores, recursos, etc.)
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT desde la cookie `JWT_TOKEN` si existe.
     * 
     * @param request Petición HTTP actual que contiene las cookies del usuario.
     * @return El valor del token JWT si la cookie está presente, `null` en caso contrario.
     */
    private String extractTokenFromCookies(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if ("JWT_TOKEN".equals(cookie.getName())) {
                return cookie.getValue(); // Retorna el valor del token encontrado
            }
        }
        return null; // Retorna `null` si la cookie no está presente
    }
}
