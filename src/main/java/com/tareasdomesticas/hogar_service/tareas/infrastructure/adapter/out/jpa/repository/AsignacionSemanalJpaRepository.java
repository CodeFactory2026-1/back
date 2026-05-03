package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.repository;

import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity.AsignacionSemanalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AsignacionSemanalJpaRepository extends JpaRepository<AsignacionSemanalEntity, Long> {

    Optional<AsignacionSemanalEntity> findTopByIdHogarOrderByFechaAsignacionDesc(Long idHogar);
}