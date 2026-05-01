package com.tareasdomesticas.hogar_service.hogares.application.port.in;

public record EliminarMiembroCommand(Long hogarId, Long idAdministrador, Long idMiembro) {}
