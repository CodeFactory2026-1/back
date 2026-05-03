package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.mapper;

import com.tareasdomesticas.hogar_service.common.domain.model.RolUsuario;
import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.entity.HogarEntity;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.entity.UsuarioEntity;

import java.util.List;

public final class HogarMapper {

    private HogarMapper() {}

    public static Hogar toDomain(HogarEntity entity) {
        UsuarioEntity adminEntity = entity.getUsuarios().stream()
                .filter(u -> u.getRolUsuario() == RolUsuario.ADMINISTRADOR)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "El hogar con id " + entity.getIdHogar() + " no tiene administrador."));

        Usuario admin = toUsuarioDomain(adminEntity);

        Hogar hogar = new Hogar(entity.getIdHogar(), entity.getNombreHogar(),
                entity.getDescripcionHogar(), admin);
        entity.getUsuarios().stream()
                .filter(u -> u.getRolUsuario() != RolUsuario.ADMINISTRADOR)
                .map(HogarMapper::toUsuarioDomain)
                .forEach(hogar::cargarMiembro);

        return hogar;
    }

    public static HogarEntity toEntity(Hogar hogar) {
        HogarEntity entity = HogarEntity.builder()
                .idHogar(hogar.getIdHogar())
                .nombreHogar(hogar.getNombreHogar())
                .descripcionHogar(hogar.getDescripcionHogar())
                .build();

        List<UsuarioEntity> usuarioEntities = hogar.getUsuarios().stream()
                .map(u -> toUsuarioEntity(u, entity))
                .toList();

        entity.getUsuarios().addAll(usuarioEntities);
        return entity;
    }

    private static Usuario toUsuarioDomain(UsuarioEntity e) {
        Usuario u = new Usuario(e.getIdUsuario(), e.getNombreUsuario(), e.getCorreoUsuario());
        if (e.getRolUsuario() == RolUsuario.ADMINISTRADOR) {
            u.convertirEnAdministrador();
        }
        if (e.getHogar() != null) {
            u.asignarHogar(e.getHogar().getIdHogar());
        }
        return u;
    }

    private static UsuarioEntity toUsuarioEntity(Usuario u, HogarEntity hogarEntity) {
        return UsuarioEntity.builder()
                .idUsuario(u.getIdUsuario())
                .nombreUsuario(u.getNombreUsuario())
                .correoUsuario(u.getCorreoUsuario())
                .rolUsuario(u.getRolUsuario())
                .hogar(hogarEntity)
                .build();
    }
}