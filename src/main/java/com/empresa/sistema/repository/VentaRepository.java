package com.empresa.sistema.repository;

import com.empresa.sistema.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    Optional<Venta> findByNumeroFactura(String numeroFactura);
}
