package com.empresa.sistema.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity @Table(name="roles")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Rol {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true, nullable=false)
    private String nombre;
    private String descripcion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="roles_permisos",
        joinColumns = @JoinColumn(name="rol_id"),
        inverseJoinColumns = @JoinColumn(name="permiso_id"))
    private Set<Permiso> permisos = new HashSet<>();
}
