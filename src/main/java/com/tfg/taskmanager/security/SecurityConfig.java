package com.tfg.taskmanager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad global para la aplicación.
 * Esta clase define las reglas de acceso, el filtro JWT y el manejo de sesiones.
 */
@Configuration // Indica que esta clase contiene configuraciones de Spring.
@RequiredArgsConstructor // Lombok genera un constructor con los atributos finales, simplificando la inyección de dependencias.
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; // Filtro de autenticación JWT que validará los tokens antes de permitir el acceso.

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     *
     * @param http Objeto HttpSecurity para configurar seguridad HTTP.
     * @return La configuración completa de seguridad.
     * @throws Exception Si ocurre algún error en la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Deshabilita la protección contra CSRF, ya que en APIs REST esta protección no es necesaria.
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define el sistema como "stateless" (sin sesiones).
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/", 
                            "/login", 
                            "/dashboard", 
                            "/css/**", 
                            "/js/**", 
                            "/images/**", 
                            "/webjars/**"
                        ).permitAll() // Permite acceso público sin autenticacion a estas rutas.
                        .requestMatchers("/api/auth/**").permitAll() // Permite acceso sin autenticación a los endpoints de autenticación.
                        .anyRequest().authenticated() // Todo lo demás requiere autenticación con JWT.
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Inserta el filtro JWT antes del filtro de autenticación por usuario/contraseña.
                .build(); // Construye la cadena de seguridad configurada.
    }

    /**
     * Define un codificador de contraseñas seguro con BCrypt.
     * Es necesario para el almacenamiento seguro de contraseñas en la base de datos.
     *
     * @return Instancia de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proporciona el AuthenticationManager necesario para la autenticación de usuarios.
     * Se usa especialmente cuando trabajamos con autenticación basada en DAO.
     *
     * @param config Configuración de autenticación de Spring Security.
     * @return Instancia de AuthenticationManager.
     * @throws Exception Si hay errores en la obtención del AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
