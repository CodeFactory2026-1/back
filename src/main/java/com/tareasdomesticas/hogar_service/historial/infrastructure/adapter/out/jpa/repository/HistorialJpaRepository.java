package com.tareasdomesticas.hogar_service.historial.infrastructure.adapter.out.jpa.repository;

import com.tareasdomesticas.hogar_service.historial.infrastructure.adapter.out.jpa.entity.EntradaHistorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialJpaRepository extends JpaRepository<EntradaHistorialEntity, Long> {

    /** Todas las actividades del hogar, más recientes primero. */
    List<EntradaHistorialEntity> findByIdHogarOrderByFechaHoraDesc(Long idHogar);

    /**
     * Actividades del hogar donde el actor fue un usuario concreto.
     * Para miembros: solo ven sus propias acciones.
     */
    List<EntradaHistorialEntity> findByIdHogarAndIdUsuarioActorOrderByFechaHoraDesc(
            Long idHogar, Long idUsuarioActor);
}
