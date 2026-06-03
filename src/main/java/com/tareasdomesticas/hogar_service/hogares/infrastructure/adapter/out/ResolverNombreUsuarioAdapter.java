package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out;

import com.tareasdomesticas.hogar_service.common.application.port.out.ResolverNombreUsuarioPort;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResolverNombreUsuarioAdapter implements ResolverNombreUsuarioPort {

    private final UsuarioJpaRepository usuarioJpaRepository;

    @Override
    public String resolverNombre(Long idUsuario) {
        if (idUsuario == null) return "Usuario desconocido";
        return usuarioJpaRepository.findById(idUsuario)
                .map(u -> u.getNombreUsuario())
                .orElse("Usuario desconocido");
    }
}
