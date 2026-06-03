package com.tareasdomesticas.hogar_service.historial.application.dto;

import java.time.LocalDateTime;

public record EntradaHistorialDTO(
                Long id,
                Long idTarea,
                String nombreTarea,
                String tipoAccion,
                Long idUsuarioActor,
                String nombreUsuarioActor,
                LocalDateTime fechaHora,
                String detalle) {
}
