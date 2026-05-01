package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.repository;

import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByCorreoUsuario(String correoUsuario);
}