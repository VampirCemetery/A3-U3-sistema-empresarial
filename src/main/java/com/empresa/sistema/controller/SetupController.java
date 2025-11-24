package com.empresa.sistema.controller;

import com.empresa.sistema.service.UsuarioService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class SetupController {

    private final UsuarioService usuarioService;

    public SetupController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/setup/admin")
    public String setupForm(Model model) {
        if (usuarioService.hayUsuarios()) {
            return "redirect:/login";
        }
        model.addAttribute("form", new AdminSetupForm());
        return "setup-admin";
    }

    @PostMapping("/setup/admin")
    public String setupSubmit(@Valid @ModelAttribute("form") AdminSetupForm form, BindingResult br, Model model) {
        System.out.println("Setup submit called for user: " + form.getUsername());
        if (usuarioService.hayUsuarios()) {
            System.out.println("Users already exist, redirecting to login");
            return "redirect:/login";
        }
        if (br.hasErrors() || !form.getPassword().equals(form.getConfirmPassword())) {
            System.out.println("Validation errors: " + br.getAllErrors());
            if (!form.getPassword().equals(form.getConfirmPassword())) {
                br.rejectValue("confirmPassword", "match", "Las contraseñas no coinciden");
            }
            return "setup-admin";
        }

        try {
            usuarioService.crearUsuarioAdminInicial(form.getUsername(), form.getEmail(), form.getPassword(),
                    form.getNombreCompleto());
            System.out.println("User created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error creating user: " + e.getMessage());
            return "setup-admin";
        }
        return "redirect:/login?setup=ok";
    }

    @Data
    public static class AdminSetupForm {
        @NotBlank
        private String username;
        @Email
        @NotBlank
        private String email;
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        private String password;
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        private String confirmPassword;
        @NotBlank
        private String nombreCompleto;
    }
}
