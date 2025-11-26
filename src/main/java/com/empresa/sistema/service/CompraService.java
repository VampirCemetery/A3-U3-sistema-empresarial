package com.empresa.sistema.service;

import com.empresa.sistema.dto.CompraDTO;
import com.empresa.sistema.entity.Compra;
import java.util.List;
import java.util.Optional;

public interface CompraService {
    List<Compra> findAll();

    Optional<Compra> findById(Long id);

    Compra crearCompra(CompraDTO compraDTO, Long usuarioId);
}
