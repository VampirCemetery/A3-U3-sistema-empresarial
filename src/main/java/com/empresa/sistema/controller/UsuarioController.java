package com.empresa.sistema.controller;

import com.empresa.sistema.entity.Usuario;
import com.empresa.sistema.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("title", "Gestión de Usuarios");
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("view", "usuarios-list");
        return "layout";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("usuario") Usuario u, BindingResult br) {
        if (br.hasErrors())
            return "usuarios-list";
        usuarioService.save(u);
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return "redirect:/usuarios";
    }
}
