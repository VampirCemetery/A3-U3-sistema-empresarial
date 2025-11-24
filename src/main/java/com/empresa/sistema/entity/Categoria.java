package com.empresa.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="categorias")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(columnDefinition="TEXT")
    private String descripcion;
    private Boolean activa = true;
}
