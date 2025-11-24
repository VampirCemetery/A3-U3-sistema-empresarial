package com.empresa.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="detalle_compras")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DetalleCompra {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name="compra_id")
    private Compra compra;
    @ManyToOne @JoinColumn(name="producto_id")
    private Producto producto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
