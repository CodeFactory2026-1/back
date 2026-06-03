package com.tareasdomesticas.hogar_service.historial.infrastructure.adapter.out.jpa.mapper;

import com.tareasdomesticas.hogar_service.historial.domain.model.EntradaHistorial;
import com.tareasdomesticas.hogar_service.historial.infrastructure.adapter.out.jpa.entity.EntradaHistorialEntity;

public final class HistorialMapper {
    private HistorialMapper() {
    }

    public static EntradaHistorial toDomain(EntradaHistorialEntity e) {
        return new EntradaHistorial(
                e.getId(),
                e.getIdHogar(),
                e.getIdTarea(),
                e.getNombreTarea(),
                e.getTipoAccion(), // ya es TipoAccion enum, no String
                e.getIdUsuarioActor(),
                e.getNombreUsuarioActor(),
                e.getFechaHora(),
                e.getDetalle());
    }

    public static EntradaHistorialEntity toEntity(EntradaHistorial d) {
        return EntradaHistorialEntity.builder()
                .id(d.getId())
                .idHogar(d.getIdHogar())
                .idTarea(d.getIdTarea())
                .nombreTarea(d.getNombreTarea())
                .tipoAccion(d.getTipoAccion()) // TipoAccion enum directamente
                .idUsuarioActor(d.getIdUsuarioActor())
                .nombreUsuarioActor(d.getNombreUsuarioActor())
                .fechaHora(d.getFechaHora())
                .detalle(d.getDetalle())
                .build();
    }
}
