package com.tfg.taskmanager.project.controller;

import com.tfg.taskmanager.project.model.dto.ProjectCreateDTO;
import com.tfg.taskmanager.project.service.ProjectService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador web para gestionar vistas relacionadas con proyectos.
 * Permite mostrar el formulario de creación de proyectos y procesar su envío.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectWebController {

    private final ProjectService projectService;

    /**
     * Muestra el formulario de creación de proyectos.
     * @param model modelo para enviar el DTO vacío a la vista
     * @return nombre de la vista
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new ProjectCreateDTO());
        return "create-project";
    }

    /**
     * Procesa el formulario de creación y persiste el nuevo proyecto.
     * @param dto datos ingresados por el usuario
     * @param request para extraer el usuario desde la cookie
     * @return redirección al dashboard
     */
    @PostMapping("/new")
    public String createProject(
            @ModelAttribute("project") @Valid ProjectCreateDTO dto,
            HttpServletRequest request) {

        String username = getCookieValue(request, "USERNAME");

        if (username == null) {
            return "redirect:/login";
        }

        projectService.createProject(dto, username);
        return "redirect:/dashboard";
    }

    /**
     * Extrae una cookie por nombre desde la solicitud HTTP.
     */
    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals(name)) {
                return c.getValue();
            }
        }
        return null;
    }
}
