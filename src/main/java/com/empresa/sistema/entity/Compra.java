package com.empresa.sistema.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.Instant;
import java.util.*;

@Entity @Table(name="compras")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Compra {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true, nullable=false)
    private String numeroFactura;
    @ManyToOne(optional=false)
    @JoinColumn(name="proveedor_id")
    private Proveedor proveedor;
    private LocalDate fechaCompra;
    private Double subtotal;
    private Double iva;
    private Double total;
    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;
    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;
    private Instant fechaCreacion = Instant.now();

    @OneToMany(mappedBy="compra", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<DetalleCompra> detalles = new ArrayList<>();

    public enum Estado { PENDIENTE, COMPLETADA, CANCELADA }
}
