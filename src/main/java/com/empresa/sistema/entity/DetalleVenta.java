package com.empresa.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="detalle_ventas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DetalleVenta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name="venta_id")
    private Venta venta;
    @ManyToOne @JoinColumn(name="producto_id")
    private Producto producto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
