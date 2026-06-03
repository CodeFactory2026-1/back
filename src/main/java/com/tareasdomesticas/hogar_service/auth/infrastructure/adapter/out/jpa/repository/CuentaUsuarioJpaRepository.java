package com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.repository;

import com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.entity.CuentaUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaUsuarioJpaRepository extends JpaRepository<CuentaUsuarioEntity, Long> {
    Optional<CuentaUsuarioEntity> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
