package com.empresa.sistema.controller;

import com.empresa.sistema.dto.CompraDTO;
import com.empresa.sistema.entity.Compra;
import com.empresa.sistema.security.CustomUserDetails;
import com.empresa.sistema.service.CompraService;
import com.empresa.sistema.service.ProductoService;
import com.empresa.sistema.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;

    @GetMapping("/dashboard/compras")
    @PreAuthorize("hasAnyRole('ADMIN','COMPRAS')")
    public String list(Model model) {
        model.addAttribute("compras", compraService.findAll());
        model.addAttribute("view", "dashboard-compras");
        return "layout";
    }

    @GetMapping("/compras/nueva")
    @PreAuthorize("hasAnyRole('ADMIN','COMPRAS')")
    public String newPurchase(Model model) {
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("view", "compras-form");
        return "layout";
    }

    @PostMapping("/compras/guardar")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN','COMPRAS')")
    public ResponseEntity<?> save(@RequestBody CompraDTO compraDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Compra compra = compraService.crearCompra(compraDTO, userDetails.getUsuario().getId());
            return ResponseEntity.ok(Map.of("id", compra.getId(), "message", "Compra registrada con éxito"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/compras/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','COMPRAS')")
    public String detail(@PathVariable Long id, Model model) {
        Compra compra = compraService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));
        model.addAttribute("compra", compra);
        model.addAttribute("view", "compras-detail");
        return "layout";
    }
}
