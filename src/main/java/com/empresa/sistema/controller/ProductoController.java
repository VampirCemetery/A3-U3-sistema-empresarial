package com.empresa.sistema.controller;

import com.empresa.sistema.entity.Producto;
import com.empresa.sistema.repository.CategoriaRepository;
import com.empresa.sistema.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventario/productos")
@PreAuthorize("hasAnyRole('ADMIN','INVENTARIO')")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;

    public ProductoController(ProductoService productoService, CategoriaRepository categoriaRepository) {
        this.productoService = productoService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("title", "Inventario - Productos");
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("view", "productos-list");
        return "layout";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("producto") Producto p, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("title", "Inventario - Productos");
            model.addAttribute("productos", productoService.findAll());
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("view", "productos-list");
            return "layout";
        }
        productoService.save(p);
        return "redirect:/inventario/productos";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        productoService.delete(id);
        return "redirect:/inventario/productos";
    }

    @PostMapping("/{id}/ajustar")
    public String ajustar(@PathVariable Long id, @RequestParam int delta, @RequestParam String motivo) {
        productoService.ajustarStock(id, delta, motivo, null);
        return "redirect:/inventario/productos";
    }
}
