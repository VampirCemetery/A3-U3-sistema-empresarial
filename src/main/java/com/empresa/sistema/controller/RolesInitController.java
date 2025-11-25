package com.empresa.sistema.controller;

import com.empresa.sistema.entity.Rol;
import com.empresa.sistema.repository.RolRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RolesInitController {

    private final RolRepository rolRepository;

    public RolesInitController(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @GetMapping("/setup/roles")
    @ResponseBody
    public String initRoles() {
        StringBuilder result = new StringBuilder("Inicializando roles del sistema:<br><br>");

        // Definir todos los roles necesarios
        String[][] rolesData = {
                { "ADMIN", "Administrador del sistema con acceso total" },
                { "INVENTARIO", "Gestión de productos e inventario" },
                { "VENTAS", "Registro y gestión de ventas" },
                { "COMPRAS", "Registro y gestión de compras" },
                { "USER", "Usuario normal con acceso de solo lectura" }
        };

        for (String[] roleData : rolesData) {
            String nombre = roleData[0];
            String descripcion = roleData[1];

            if (rolRepository.findByNombre(nombre).isEmpty()) {
                Rol rol = new Rol();
                rol.setNombre(nombre);
                rol.setDescripcion(descripcion);
                rolRepository.save(rol);
                result.append("✓ Creado rol: ").append(nombre).append("<br>");
            } else {
                result.append("○ Rol ya existe: ").append(nombre).append("<br>");
            }
        }

        result.append("<br>Total de roles en el sistema: ").append(rolRepository.count());
        result.append("<br><br><a href='/usuarios'>Ir a Gestión de Usuarios</a>");

        return result.toString();
    }
}
