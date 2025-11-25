package com.empresa.sistema.controller;

import com.empresa.sistema.entity.Usuario;
import com.empresa.sistema.service.UsuarioService;
import com.empresa.sistema.repository.RolRepository;
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
    private final RolRepository rolRepository;

    public UsuarioController(UsuarioService usuarioService, RolRepository rolRepository) {
        this.usuarioService = usuarioService;
        this.rolRepository = rolRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("title", "Gestión de Usuarios");
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolRepository.findAll());
        model.addAttribute("view", "usuarios-list");
        return "layout";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("usuario") Usuario u, BindingResult br,
            @RequestParam(required = false) Long rolId) {
        if (br.hasErrors())
            return "usuarios-list";

        // Si se seleccionó un rol, asignarlo al usuario
        if (rolId != null) {
            rolRepository.findById(rolId).ifPresent(rol -> {
                u.setRoles(new java.util.HashSet<>(java.util.Arrays.asList(rol)));
            });
        }

        usuarioService.save(u);
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            usuarioService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar el usuario porque tiene registros asociados (ventas o compras).");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
