package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.repository;

import com.tareasdomesticas.hogar_service.tareas.domain.model.DificultadTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.PrioridadTarea;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity.TareaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TareaJpaRepository extends JpaRepository<TareaEntity, Long> {

    List<TareaEntity> findByIdHogar(Long idHogar);

    @Query("""
            SELECT t FROM TareaEntity t
            WHERE t.idHogar = :hogarId
              AND t.idTarea NOT IN :idsExcluidas
            """)
    List<TareaEntity> findPendientesByHogar(@Param("hogarId") Long hogarId,
                                             @Param("idsExcluidas") List<Long> idsExcluidas);

    @Query("""
            SELECT t FROM TareaEntity t
            WHERE t.idHogar = :hogarId
              AND (:nombre IS NULL OR LOWER(t.nombreTarea) LIKE LOWER(CONCAT('%', :nombre, '%')))
              AND (:dificultad IS NULL OR t.dificultadTarea = :dificultad)
              AND (:prioridad IS NULL OR t.prioridadTarea = :prioridad)
            """)
    List<TareaEntity> filtrar(@Param("hogarId") Long hogarId,
                               @Param("nombre") String nombre,
                               @Param("dificultad") DificultadTarea dificultad,
                               @Param("prioridad") PrioridadTarea prioridad);

    @Query("""
            SELECT COUNT(t) > 0 FROM TareaEntity t
            WHERE LOWER(t.nombreTarea) = LOWER(:nombre)
              AND t.idHogar = :hogarId
              AND FUNCTION('date_part', 'week', t.fechaLimite) =
                  FUNCTION('date_part', 'week', CAST(:fechaLimite AS timestamp))
            """)
    boolean existePorNombreYSemana(@Param("nombre") String nombre,
                                    @Param("fechaLimite") LocalDateTime fechaLimite,
                                    @Param("hogarId") Long hogarId);
}
