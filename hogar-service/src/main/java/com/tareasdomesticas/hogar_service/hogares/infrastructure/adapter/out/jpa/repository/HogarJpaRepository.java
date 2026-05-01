package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.repository;

import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.entity.HogarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HogarJpaRepository extends JpaRepository<HogarEntity, Long> {

    @Query("SELECT h FROM HogarEntity h JOIN h.usuarios u WHERE u.idUsuario = :usuarioId")
    Optional<HogarEntity> findByUsuariosIdUsuario(@Param("usuarioId") Long usuarioId);
    @Query("SELECT h FROM HogarEntity h JOIN h.usuarios u WHERE u.correoUsuario = :correo")
    Optional<HogarEntity> findByUsuariosCorreoUsuario(@Param("correo") String correo);
}