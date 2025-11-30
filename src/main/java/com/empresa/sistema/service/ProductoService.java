package com.empresa.sistema.service;

import com.empresa.sistema.entity.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> findAll();
    List<Producto> findAllIncludingInactive();
    Optional<Producto> findById(Long id);
    Producto save(Producto p);
    void delete(Long id);
    void reactivar(Long id);
    void ajustarStock(Long productoId, int delta, String motivo, Long usuarioId);
}
