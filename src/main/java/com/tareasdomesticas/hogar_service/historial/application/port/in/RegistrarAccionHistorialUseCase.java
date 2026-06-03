package com.tareasdomesticas.hogar_service.historial.application.port.in;

import com.tareasdomesticas.hogar_service.historial.domain.model.TipoAccion;

/**
 * Puerto de entrada usado por otros servicios para registrar acciones
 * auditables.
 */
public interface RegistrarAccionHistorialUseCase {
    void registrar(Long idHogar, Long idTarea, String nombreTarea,
            TipoAccion tipoAccion, Long idUsuarioActor, String nombreUsuarioActor,
            String detalle);
}
