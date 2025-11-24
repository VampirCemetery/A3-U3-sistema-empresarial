package com.empresa.sistema.service;

import com.empresa.sistema.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    boolean hayUsuarios();
    Usuario crearUsuarioAdminInicial(String username, String email, String password, String nombreCompleto);
    Optional<Usuario> findById(Long id);
    List<Usuario> findAll();
    Usuario save(Usuario u);
    void delete(Long id);
}
