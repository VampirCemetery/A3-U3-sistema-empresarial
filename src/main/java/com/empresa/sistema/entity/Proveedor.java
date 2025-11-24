package com.empresa.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="proveedores")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Proveedor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true, nullable=false)
    private String ruc;
    @Column(nullable=false)
    private String nombre;
    @Column(columnDefinition="TEXT")
    private String direccion;
    private String telefono;
    private String email;
    private Boolean activo = true;
}
