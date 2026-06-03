package com.tareasdomesticas.hogar_service.invitaciones.infrastructure.adapter.out.jpa.mapper;

import com.tareasdomesticas.hogar_service.invitaciones.domain.model.Invitacion;
import com.tareasdomesticas.hogar_service.invitaciones.infrastructure.adapter.out.jpa.entity.InvitacionEntity;

public final class InvitacionMapper {
    private InvitacionMapper() {}

    public static Invitacion toDomain(InvitacionEntity e) {
        return Invitacion.reconstruir(
                e.getId(),
                e.getIdHogar(),
                e.getNombreInvitado(),
                e.getCorreoInvitado(),
                e.getEstado(),
                e.getCreatedAt(),
                e.getFechaRespuesta());   // ← antes siempre llegaba null
    }

    public static InvitacionEntity toEntity(Invitacion i) {
        return InvitacionEntity.builder()
                .id(i.getId())
                .idHogar(i.getIdHogar())
                .nombreInvitado(i.getNombreInvitado())
                .correoInvitado(i.getCorreoInvitado())
                .estado(i.getEstado())
                .fechaRespuesta(i.getFechaRespuesta())   // ← antes siempre null → violaba CHECK
                .build();
    }
}
