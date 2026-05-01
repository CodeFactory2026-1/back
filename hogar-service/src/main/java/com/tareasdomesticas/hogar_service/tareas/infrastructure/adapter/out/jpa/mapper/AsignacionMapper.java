package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.mapper;

import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
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
                e.isExcedente());
    }

    public static AsignacionSemanalTareaEntity toEntity(
            AsignacionSemanalTarea ast, AsignacionSemanalEntity asignacionEntity) {
        AsignacionSemanalTareaId id = new AsignacionSemanalTareaId(ast.getIdAsignacion(), ast.getIdTarea());
        EstadoTarea estadoFinal = ast.getEstado();
        if (ast.getIdUsuarioAsignado() == null
                && estadoFinal != EstadoTarea.FINALIZADO) {
            estadoFinal = EstadoTarea.PENDIENTE;
        }
        return AsignacionSemanalTareaEntity.builder()
                .id(id)
                .asignacion(asignacionEntity)
                .idUsuarioAsignado(ast.getIdUsuarioAsignado())
                .estadoTarea(ast.getEstado())
                .excedente(ast.isExcedente())
                .build();
    }
}
