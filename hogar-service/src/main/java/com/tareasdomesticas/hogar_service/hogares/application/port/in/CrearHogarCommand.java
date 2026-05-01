package com.tareasdomesticas.hogar_service.hogares.application.port.in;

public record CrearHogarCommand(
    Long usuarioId,
    String nombreUsuario,
    String correoUsuario,
    String nombreHogar,
    String descripcion
) {}