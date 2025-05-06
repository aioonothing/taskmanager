package com.tfg.taskmanager.auth.controller;

import com.tfg.taskmanager.auth.dto.LoginRequest;
import com.tfg.taskmanager.auth.dto.LoginResponse;
import com.tfg.taskmanager.auth.service.AuthClientService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsable de manejar el proceso de autenticación de usuarios.
 * 
 * Interactúa con el microservicio `auth-service` para validar credenciales y obtener un token JWT.
 * Luego, almacena el JWT en una cookie segura para gestionar la sesión del usuario.
 * 
 * Principios seguidos:
 * - **Seguridad:** Cookies `HttpOnly` para proteger el JWT de accesos desde JavaScript malicioso.
 * - **Modularidad:** Delegación de la autenticación a `AuthClientService`.
 * - **Mantenibilidad:** Separación clara de la lógica de presentación y negocio.
 */
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    /** Servicio para comunicarse con el microservicio de autenticación */
    private final AuthClientService authClientService;

    /**
     * Muestra el formulario de login.
     * 
     * @param model Modelo de datos para la vista.
     * @return Nombre de la plantilla de login.
     */
    @GetMapping
    public String showLoginForm(Model model) {
        // Se añade un objeto vacío de `LoginRequest` para que el formulario lo rellene
        model.addAttribute("loginRequest", new LoginRequest());
        return "login"; // Devuelve la vista 'login.html'
    }

    /**
     * Procesa la solicitud de inicio de sesión.
     * 
     * @param loginRequest Datos de autenticación ingresados por el usuario.
     * @param model Modelo para enviar mensajes de error.
     * @param response Respuesta HTTP usada para gestionar cookies de sesión.
     * @return Redirección al dashboard si la autenticación es exitosa, sino regresa al login.
     */
    @PostMapping
    public String processLogin(
            @ModelAttribute("loginRequest") LoginRequest loginRequest,
            Model model,
            HttpServletResponse response) {

        try {
            // Se envía la solicitud de login al microservicio `auth-service`
            LoginResponse login = authClientService.login(loginRequest);

            // Configuración de cookies de sesión segura
            createSessionCookies(response, login.getToken(), login.getUsername());

            // Redirige al usuario al dashboard tras iniciar sesión con éxito
            return "redirect:/dashboard";

        } catch (RuntimeException ex) {
            // En caso de error, se devuelve el mensaje a la vista de login
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }

    /**
     * Crea y configura cookies de sesión segura para almacenar el JWT y el usuario autenticado.
     * 
     * @param response Objeto de respuesta HTTP donde se almacenarán las cookies.
     * @param token JWT generado por el `auth-service`.
     * @param username Nombre de usuario autenticado.
     */
    private void createSessionCookies(HttpServletResponse response, String token, String username) {
        // Cookie que almacena el JWT, protegida contra accesos desde JavaScript
        Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
        jwtCookie.setHttpOnly(true); // Previene acceso desde JavaScript para mayor seguridad
        jwtCookie.setPath("/"); // Disponible en toda la aplicación
        jwtCookie.setMaxAge(60 * 60); // Expira en 1 hora
        // jwtCookie.setSecure(true); // **Activar en producción si se usa HTTPS**

        // Cookie que almacena el nombre del usuario, no es sensible
        Cookie userCookie = new Cookie("USERNAME", username);
        userCookie.setPath("/");
        userCookie.setMaxAge(60 * 60); // Expira en 1 hora

        // Se añaden las cookies a la respuesta HTTP
        response.addCookie(jwtCookie);
        response.addCookie(userCookie);
    }
}
