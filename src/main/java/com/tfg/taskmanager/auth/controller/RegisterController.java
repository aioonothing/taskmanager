package com.tfg.taskmanager.auth.controller;

import com.tfg.taskmanager.auth.dto.RegisterRequest;
import com.tfg.taskmanager.auth.service.AuthClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador web para registro de nuevos usuarios desde el frontend.
 * Se comunica con el microservicio auth-service para delegar el registro.
 */
@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final AuthClientService authClientService;

    /**
     * Muestra el formulario de registro.
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    /**
     * Procesa el formulario de registro y llama al microservicio.
     */
    @PostMapping("/register")
    public String processRegister(
            @ModelAttribute("registerRequest") @Valid RegisterRequest request,
            Model model) {

        try {
            authClientService.register(request);
            return "redirect:/login";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}
