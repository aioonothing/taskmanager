package com.tfg.taskmanager.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Servicio encargado de la generación y validación de tokens JWT.
 * Utilizado para autenticar y autorizar solicitudes dentro del sistema.
 */
@Slf4j  // Permite el registro de eventos y errores relacionados con JWT en el sistema de logs.
@Service // Define esta clase como un componente de servicio en Spring, permitiendo su inyección automática.
public class JwtService {

    @Value("${jwt.secret}")
    private String secret; // Recupera la clave secreta desde application.properties/env.

    private Key key; // Almacena la clave utilizada para firmar y verificar los tokens.

    /**
     * Inicializa la clave de firma utilizando el algoritmo HMAC.
     * Este método se ejecuta automáticamente después de la creación de la instancia.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     * @param token JWT del cual se extraerá el nombre de usuario.
     * @return Nombre de usuario contenido en el token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae una claim específica del token mediante una función resolutiva.
     * @param token JWT del cual se extraerá la claim.
     * @param resolver Función que determina qué claim extraer.
     * @param <T> Tipo de dato esperado de la claim.
     * @return Valor de la claim especificada.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Valida si el token es correcto y está vigente.
     * Verifica que la firma del JWT sea válida y que el token no haya expirado.
     * @param token JWT que será validado.
     * @return true si el token es válido, false si es inválido o ha expirado.
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token); // Si el token es inválido o corrupto, lanzará una excepción.
            return !isTokenExpired(token); // Valida que el token no haya expirado.
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token inválido: {}", e.getMessage()); // Registra detalles en logs para auditoría.
            return false;
        }
    }

    /**
     * Verifica si un token ha expirado evaluando su fecha de vencimiento.
     * @param token JWT a verificar.
     * @return true si el token ha expirado, false si sigue siendo válido.
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Extrae todas las claims contenidas en el token, validando su firma.
     * @param token JWT del cual se extraerán las claims.
     * @return Objeto Claims con toda la información del token.
     * @throws JwtException si el token tiene una firma inválida o está corrupto.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.key) // Configura la clave de firma para validar la autenticidad del token.
                .build()
                .parseClaimsJws(token) // Parsea el token y valida su integridad.
                .getBody(); // Devuelve las claims contenidas en el token.
    }
}
