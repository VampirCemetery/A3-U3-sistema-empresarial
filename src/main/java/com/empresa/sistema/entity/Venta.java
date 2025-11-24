package com.empresa.sistema.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.Instant;
import java.util.*;

@Entity @Table(name="ventas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Venta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true, nullable=false)
    private String numeroFactura;
    @ManyToOne @JoinColumn(name="cliente_id")
    private Cliente cliente;
    private LocalDate fechaVenta;
    private Double subtotal;
    private Double iva;
    private Double total;
    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;
    @ManyToOne @JoinColumn(name="usuario_id")
    private Usuario usuario;
    private Instant fechaCreacion = Instant.now();

    @OneToMany(mappedBy="venta", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public enum Estado { PENDIENTE, COMPLETADA, CANCELADA }
}
