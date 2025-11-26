package com.empresa.sistema.controller;

import com.empresa.sistema.entity.Proveedor;
import com.empresa.sistema.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','COMPRAS')")
    public String list(Model model) {
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("view", "proveedores-list");
        return "layout";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','COMPRAS')")
    public String save(@ModelAttribute Proveedor proveedor, RedirectAttributes ra) {
        proveedorService.save(proveedor);
        ra.addFlashAttribute("success", "Proveedor guardado correctamente");
        return "redirect:/proveedores";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            proveedorService.deleteById(id);
            ra.addFlashAttribute("success", "Proveedor eliminado");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se puede eliminar el proveedor (posiblemente tenga compras asociadas)");
        }
        return "redirect:/proveedores";
    }
}
