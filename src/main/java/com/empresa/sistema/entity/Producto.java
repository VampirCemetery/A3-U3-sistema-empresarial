package com.empresa.sistema.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="productos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String codigo;

    @Column(nullable=false)
    private String nombre;

    @Column(columnDefinition="TEXT")
    private String descripcion;

    private Double precioCompra;
    @Column(nullable=false)
    private Double precioVenta;
    private Integer stockActual = 0;
    private Integer stockMinimo = 5;

    @ManyToOne
    @JoinColumn(name="categoria_id")
    private Categoria categoria;

    private Boolean activo = true;
    private Instant fechaCreacion = Instant.now();
}
