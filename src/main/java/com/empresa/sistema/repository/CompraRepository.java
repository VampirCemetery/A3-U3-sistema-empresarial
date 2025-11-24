package com.empresa.sistema.repository;

import com.empresa.sistema.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    Optional<Compra> findByNumeroFactura(String numeroFactura);
}
