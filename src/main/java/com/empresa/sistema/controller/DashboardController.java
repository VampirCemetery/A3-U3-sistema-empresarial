package com.empresa.sistema.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final com.empresa.sistema.repository.ProductoRepository productoRepository;
    private final com.empresa.sistema.repository.ClienteRepository clienteRepository;
    private final com.empresa.sistema.repository.VentaRepository ventaRepository;
    private final com.empresa.sistema.repository.CompraRepository compraRepository;

    public DashboardController(com.empresa.sistema.repository.ProductoRepository productoRepository,
            com.empresa.sistema.repository.ClienteRepository clienteRepository,
            com.empresa.sistema.repository.VentaRepository ventaRepository,
            com.empresa.sistema.repository.CompraRepository compraRepository) {
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
        this.compraRepository = compraRepository;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/dashboard/admin";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard/admin")
    public String admin(Model model) {
        model.addAttribute("title", "Dashboard Admin");
        model.addAttribute("view", "dashboard-admin");

        // Populate stats
        model.addAttribute("productos", productoRepository.count());
        model.addAttribute("clientes", clienteRepository.count());
        model.addAttribute("ventasMes", ventaRepository.count()); // TODO: Filter by month and sum total
        model.addAttribute("comprasMes", compraRepository.count()); // TODO: Filter by month and sum total

        return "layout";
    }

    @PreAuthorize("hasRole('INVENTARIO') or hasRole('ADMIN')")
    @GetMapping("/dashboard/inventario")
    public String inventario(Model model) {
        return "redirect:/inventario/productos";
    }

    @PreAuthorize("hasRole('VENTAS') or hasRole('ADMIN')")
    @GetMapping("/dashboard/ventas")
    public String ventas(Model model) {
        model.addAttribute("title", "Dashboard Ventas");
        model.addAttribute("view", "dashboard-ventas");
        return "layout";
    }

    @PreAuthorize("hasRole('COMPRAS') or hasRole('ADMIN')")
    @GetMapping("/dashboard/compras")
    public String compras(Model model) {
        model.addAttribute("title", "Dashboard Compras");
        model.addAttribute("view", "dashboard-compras");
        return "layout";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        model.addAttribute("title", "Catálogo de Productos");
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("view", "catalogo");
        return "layout";
    }
}
