package com.empresa.sistema.service;

import com.empresa.sistema.dto.VentaDTO;
import com.empresa.sistema.entity.Venta;
import java.util.List;
import java.util.Optional;

public interface VentaService {
    List<Venta> findAll();

    Optional<Venta> findById(Long id);

    Venta crearVenta(VentaDTO ventaDTO, Long usuarioId);
}
