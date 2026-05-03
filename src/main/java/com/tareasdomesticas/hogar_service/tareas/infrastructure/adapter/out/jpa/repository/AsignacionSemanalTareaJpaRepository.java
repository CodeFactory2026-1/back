package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.repository;

import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity.AsignacionSemanalTareaEntity;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity.AsignacionSemanalTareaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AsignacionSemanalTareaJpaRepository
        extends JpaRepository<AsignacionSemanalTareaEntity, AsignacionSemanalTareaId> {

    List<AsignacionSemanalTareaEntity> findByIdIdAsignacion(Long idAsignacion);

    List<AsignacionSemanalTareaEntity> findByIdIdAsignacionAndIdUsuarioAsignado(
            Long idAsignacion, Long idUsuarioAsignado);

    @Query("""
            SELECT ast FROM AsignacionSemanalTareaEntity ast
            JOIN ast.asignacion a
            WHERE ast.idUsuarioAsignado = :idUsuario
              AND a.idHogar = :hogarId
            """)
    List<AsignacionSemanalTareaEntity> findByUsuarioAndHogar(
            @Param("idUsuario") Long idUsuario,
            @Param("hogarId")   Long hogarId);

    @Query("""
            SELECT ast FROM AsignacionSemanalTareaEntity ast
            JOIN ast.asignacion a
            WHERE a.idHogar = :hogarId
              AND ast.excedente = true
              AND a.idAsignacion = (
                  SELECT MAX(a2.idAsignacion) FROM AsignacionSemanalEntity a2
                  WHERE a2.idHogar = :hogarId
              )
            """)
    List<AsignacionSemanalTareaEntity> findExcedentesByHogar(@Param("hogarId") Long hogarId);

    @Query("""
            SELECT ast FROM AsignacionSemanalTareaEntity ast
            JOIN ast.asignacion a
            WHERE ast.id.idTarea = :idTarea
              AND a.idHogar = :hogarId
              AND a.idAsignacion = (
                  SELECT MAX(a2.idAsignacion) FROM AsignacionSemanalEntity a2
                  WHERE a2.idHogar = :hogarId
              )
            """)
    Optional<AsignacionSemanalTareaEntity> findActivaByTareaAndHogar(
            @Param("idTarea")  Long idTarea,
            @Param("hogarId")  Long hogarId);
}