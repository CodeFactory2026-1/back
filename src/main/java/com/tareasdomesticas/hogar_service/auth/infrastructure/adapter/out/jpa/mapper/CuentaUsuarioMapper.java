package com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.mapper;

import com.tareasdomesticas.hogar_service.auth.domain.model.CuentaUsuario;
import com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.entity.CuentaUsuarioEntity;
import com.tareasdomesticas.hogar_service.common.domain.model.RolUsuario;

public final class CuentaUsuarioMapper {

    private CuentaUsuarioMapper() {}

    public static CuentaUsuario toDomain(CuentaUsuarioEntity e) {
        // Pasar rol para que IniciarSesionService pueda devolverlo en LoginResultDTO (HU2)
        String rol = e.getRolUsuario() != null ? e.getRolUsuario().name() : "MIEMBRO";
        return CuentaUsuario.reconstruir(
                e.getId(),
                e.getNombre(),
                e.getCorreo(),
                e.getHashContrasena(),
                e.getIdHogar(),
                rol);
    }

    public static CuentaUsuarioEntity toEntity(CuentaUsuario c) {
        return CuentaUsuarioEntity.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .correo(c.getCorreo())
                .hashContrasena(c.getHashContrasena())
                .rolUsuario(RolUsuario.MIEMBRO)   // nuevos usuarios siempre MIEMBRO
                .idHogar(c.getIdHogar())
                .build();
    }
}
