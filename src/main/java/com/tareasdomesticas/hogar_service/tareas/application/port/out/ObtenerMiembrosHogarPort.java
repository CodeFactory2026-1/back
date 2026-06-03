package com.tareasdomesticas.hogar_service.tareas.application.port.out;

import java.util.List;

public interface ObtenerMiembrosHogarPort {

    List<Long> obtenerIdsUsuarios(Long hogarId);

    /** Devuelve pares (id, nombre) de todos los miembros del hogar. */
    List<MiembroInfo> obtenerMiembros(Long hogarId);

    record MiembroInfo(Long idUsuario, String nombreUsuario) {}
}
