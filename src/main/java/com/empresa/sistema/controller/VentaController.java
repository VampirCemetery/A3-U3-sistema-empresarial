package com.empresa.sistema.controller;

import com.empresa.sistema.dto.VentaDTO;
import com.empresa.sistema.entity.Venta;
import com.empresa.sistema.security.CustomUserDetails;
import com.empresa.sistema.service.ClienteService;
import com.empresa.sistema.service.ProductoService;
import com.empresa.sistema.service.VentaService;
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
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    @GetMapping("/dashboard/ventas")
    @PreAuthorize("hasAnyRole('ADMIN','VENTAS')")
    public String list(Model model) {
        model.addAttribute("ventas", ventaService.findAll());
        model.addAttribute("view", "dashboard-ventas");
        return "layout";
    }

    @GetMapping("/ventas/nueva")
    @PreAuthorize("hasAnyRole('ADMIN','VENTAS')")
    public String newSale(Model model) {
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("view", "ventas-form");
        return "layout";
    }

    @PostMapping("/ventas/guardar")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN','VENTAS')")
    public ResponseEntity<?> save(@RequestBody VentaDTO ventaDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Venta venta = ventaService.crearVenta(ventaDTO, userDetails.getUsuario().getId());
            return ResponseEntity.ok(Map.of("id", venta.getId(), "message", "Venta registrada con éxito"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/ventas/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENTAS')")
    public String detail(@PathVariable Long id, Model model) {
        Venta venta = ventaService.findById(id).orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
        model.addAttribute("venta", venta);
        model.addAttribute("view", "ventas-detail");
        return "layout";
    }
}
