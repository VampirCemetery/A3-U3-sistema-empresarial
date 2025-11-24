package com.empresa.sistema.controller;

import com.empresa.sistema.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        // Redirect to setup if no users exist
        if (!usuarioService.hayUsuarios()) {
            return "redirect:/setup/admin";
        }
        return "login";
    }
}
