package com.empresa.sistema.service.impl;

import com.empresa.sistema.entity.Permiso;
import com.empresa.sistema.entity.Rol;
import com.empresa.sistema.entity.Usuario;
import com.empresa.sistema.repository.PermisoRepository;
import com.empresa.sistema.repository.RolRepository;
import com.empresa.sistema.repository.UsuarioRepository;
import com.empresa.sistema.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final PasswordEncoder encoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            PermisoRepository permisoRepository, PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
        this.encoder = encoder;
    }

    @Override
    public boolean hayUsuarios() {
        return usuarioRepository.count() > 0;
    }

    @Override
    public Usuario crearUsuarioAdminInicial(String username, String email, String password, String nombreCompleto) {
        Rol admin = rolRepository.findByNombre("ADMIN").orElseGet(() -> {
            Rol r = new Rol();
            r.setNombre("ADMIN");
            r.setDescripcion("Administrador del sistema");
            return rolRepository.save(r);
        });
        // Cargar todos los permisos y asignarlos al rol
        List<Permiso> allPerms = permisoRepository.findAll();
        admin.setPermisos(new HashSet<>(allPerms));
        rolRepository.save(admin);

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(encoder.encode(password));
        u.setNombreCompleto(nombreCompleto);
        Set<Rol> roles = new HashSet<>();
        roles.add(admin);
        u.setRoles(roles);
        return usuarioRepository.save(u);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario save(Usuario u) {
        if (u.getId() == null) {
            if (u.getPassword() != null && !u.getPassword().isEmpty()) {
                u.setPassword(encoder.encode(u.getPassword()));
            }
            if (u.getRoles() == null || u.getRoles().isEmpty()) {
                Rol defaultRol = rolRepository.findByNombre("USER").orElseGet(() -> {
                    Rol r = new Rol();
                    r.setNombre("USER");
                    r.setDescripcion("Usuario Normal");
                    return rolRepository.save(r);
                });
                Set<Rol> roles = new HashSet<>();
                roles.add(defaultRol);
                u.setRoles(roles);
            }
        }
        return usuarioRepository.save(u);
    }

    @Override
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
        usuarioRepository.flush();
    }
}
