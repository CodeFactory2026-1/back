package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.mapper;

import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity.*;

public final class TareaMapper {

    private TareaMapper() {
    }

    public static Tarea toDomain(TareaEntity e) {
        return Tarea.reconstruir(
                e.getIdTarea(),
                e.getIdHogar(),
                e.getNombreTarea(),
                e.getDescripcionTarea(),
                e.getFotoTarea(),
                e.getFechaLimite(),
                e.getDificultadTarea(),
                e.getPrioridadTarea());
    }

    public static TareaEntity toEntity(Tarea t) {
        return TareaEntity.builder()
                .idTarea(t.getIdTarea())
                .idHogar(t.getIdHogar())
                .nombreTarea(t.getNombreTarea())
                .descripcionTarea(t.getDescripcionTarea())
                .fotoTarea(t.getFotoTarea())
                .fechaLimite(t.getFechaLimite())
                .dificultadTarea(t.getDificultad())
                .prioridadTarea(t.getPrioridad())
                .build();
    }
}