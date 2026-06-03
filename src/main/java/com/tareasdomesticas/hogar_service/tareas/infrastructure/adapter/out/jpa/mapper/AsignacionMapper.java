package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.mapper;

import com.tareasdomesticas.hogar_service.tareas.domain.model.AsignacionSemanal;
import com.tareasdomesticas.hogar_service.tareas.domain.model.AsignacionSemanalTarea;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity.*;

public final class AsignacionMapper {

    private AsignacionMapper() {
    }

    public static AsignacionSemanal toDomain(AsignacionSemanalEntity e) {
        return new AsignacionSemanal(e.getIdAsignacion(), e.getIdHogar(), e.getFechaAsignacion());
    }

    public static AsignacionSemanalEntity toEntity(AsignacionSemanal a) {
        return AsignacionSemanalEntity.builder()
                .idAsignacion(a.getIdAsignacion())
                .idHogar(a.getIdHogar())
                .fechaAsignacion(a.getFechaAsignacion())
                .build();
    }

    public static AsignacionSemanalTarea toDomain(AsignacionSemanalTareaEntity e) {
        return AsignacionSemanalTarea.reconstruir(
                e.getId().getIdAsignacion(),
                e.getId().getIdTarea(),
                e.getIdUsuarioAsignado(),
                e.getEstadoTarea(),
                e.isExcedente(),
                e.getIdUsuarioFinalizador(),
                e.getFechaUltimoCambioEstado());
    }

    public static AsignacionSemanalTareaEntity toEntity(
            AsignacionSemanalTarea ast, AsignacionSemanalEntity asignacionEntity) {
        // El mapper no toma decisiones de negocio sobre el estado.
        // La regla "sin usuario → PENDIENTE" ya la aplica
        // AsignacionSemanalTarea.liberarResponsable()
        // antes de llegar aquí. El mapper solo convierte lo que el dominio ya decidió.
        AsignacionSemanalTareaId id = new AsignacionSemanalTareaId(
                ast.getIdAsignacion(), ast.getIdTarea());
        return AsignacionSemanalTareaEntity.builder()
                .id(id)
                .asignacion(asignacionEntity)
                .idUsuarioAsignado(ast.getIdUsuarioAsignado())
                .estadoTarea(ast.getEstado())
                .excedente(ast.isExcedente())
                .idUsuarioFinalizador(ast.getIdUsuarioFinalizador())
                .fechaUltimoCambioEstado(ast.getFechaUltimoCambioEstado())
                .build();
    }
}
