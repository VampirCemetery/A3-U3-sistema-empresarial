package com.empresa.sistema.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.*;

@Entity @Table(name="usuarios")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false, length=50)
    private String username;

    @Column(unique=true, nullable=false, length=100)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(name="nombre_completo", nullable=false, length=100)
    private String nombreCompleto;

    private Boolean activo = true;

    @Column(name="fecha_creacion")
    private Instant fechaCreacion = Instant.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="usuarios_roles",
        joinColumns = @JoinColumn(name="usuario_id"),
        inverseJoinColumns = @JoinColumn(name="rol_id"))
    private Set<Rol> roles = new HashSet<>();
}
