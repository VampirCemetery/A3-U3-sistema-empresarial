package com.empresa.sistema.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="movimientos_inventario")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MovimientoInventario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="producto_id")
    private Producto producto;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento;

    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockActual;
    private String motivo;

    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    private Instant fechaMovimiento = Instant.now();

    public enum TipoMovimiento { ENTRADA, SALIDA, AJUSTE }
}
