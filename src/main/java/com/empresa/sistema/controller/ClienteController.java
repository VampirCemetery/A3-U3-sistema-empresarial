package com.empresa.sistema.controller;

import com.empresa.sistema.entity.Cliente;
import com.empresa.sistema.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENTAS')")
    public String list(Model model) {
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("view", "clientes-list");
        return "layout";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','VENTAS')")
    public String save(@ModelAttribute Cliente cliente, RedirectAttributes ra) {
        clienteService.save(cliente);
        ra.addFlashAttribute("success", "Cliente guardado correctamente");
        return "redirect:/clientes";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            clienteService.deleteById(id);
            ra.addFlashAttribute("success", "Cliente eliminado");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se puede eliminar el cliente (posiblemente tenga ventas asociadas)");
        }
        return "redirect:/clientes";
    }
}
