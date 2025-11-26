package com.empresa.sistema.repository;

import com.empresa.sistema.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByCodigo(String codigo);

    java.util.List<Producto> findByActivoTrue();
}
