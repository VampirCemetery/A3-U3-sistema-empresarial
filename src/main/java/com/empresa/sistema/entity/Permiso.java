package com.empresa.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="permisos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Permiso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String modulo;
    private String accion;
    private String descripcion;
}
