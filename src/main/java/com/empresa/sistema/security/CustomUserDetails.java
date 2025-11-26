package com.empresa.sistema.security;

import com.empresa.sistema.entity.Permiso;
import com.empresa.sistema.entity.Rol;
import com.empresa.sistema.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomUserDetails implements UserDetails {
    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> auths = new HashSet<>();
        for (Rol rol : usuario.getRoles()) {
            auths.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
            for (Permiso p : rol.getPermisos()) {
                auths.add(new SimpleGrantedAuthority(p.getModulo() + ":" + p.getAccion()));
            }
        }
        return auths;
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(usuario.getActivo());
    }

    public String getNombreCompleto() {
        return usuario.getNombreCompleto();
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
