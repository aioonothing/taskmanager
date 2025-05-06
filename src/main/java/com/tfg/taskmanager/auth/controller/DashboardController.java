package com.tfg.taskmanager.auth.controller;

import com.tfg.taskmanager.project.model.dto.ProjectDTO;
import com.tfg.taskmanager.project.service.ProjectService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controlador responsable de mostrar la vista principal (dashboard) del usuario autenticado.
 * 
 * Este controlador se encarga de:
 * - Verificar que el usuario haya iniciado sesión mediante una cookie de autenticación.
 * - Extraer el nombre de usuario desde la cookie y enviarlo a la vista.
 * - Redirigir al login si el usuario no está autenticado.
 * 
 *   Seguridad:
 * - Se basa en el uso de cookies HTTP, lo que implica que el JWT debe haber sido almacenado previamente.
 * - No verifica directamente el token JWT, sino que depende de la cookie USERNAME.
 */
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ProjectService projectService; // Servicio para recuperar los proyectos del usuario

    /**
     * Muestra la vista principal (dashboard) del usuario autenticado.
     * 
     * @param request Objeto HTTP para obtener las cookies del usuario.
     * @param model Modelo utilizado para pasar atributos a la vista.
     * @return Nombre de la vista del dashboard o redirección al login si no hay sesión.
     */
    @GetMapping("/dashboard")
    public String showDashboard(HttpServletRequest request, Model model) {
        // Obtiene el nombre de usuario desde la cookie USERNAME
        String username = getCookieValue(request, "USERNAME");

        // Si el usuario no tiene la cookie, se redirige al login
        if (username == null) {
            return "redirect:/login";
        }

        // Se añade el nombre de usuario al modelo para mostrarlo en la vista
        model.addAttribute("username", username);

        // Recupera todos los proyectos asociados al usuario autenticado
        List<ProjectDTO> projects = projectService.getAllByOwner(username);
        model.addAttribute("projects", projects); // Se pasan a la vista para ser renderizados

        return "dashboard"; // Retorna la vista del dashboard
    }

    /**
     * Utilidad para leer el valor de una cookie por nombre.
     * 
     * @param request Objeto HTTP que contiene las cookies enviadas por el cliente.
     * @param name Nombre de la cookie que se quiere obtener.
     * @return Valor de la cookie si existe, `null` si no se encuentra.
     */
    private String getCookieValue(HttpServletRequest request, String name) {
        // Si no hay cookies en la solicitud, devuelve null
        if (request.getCookies() == null) return null;

        // Itera sobre las cookies buscando la que coincida con el nombre solicitado
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals(name)) {
                return c.getValue(); // Retorna el valor de la cookie encontrada
            }
        }

        // Si la cookie no existe, retorna null
        return null;
    }
}
