package com.tareasdomesticas.hogar_service.hogares.application.port.in;

public record AgregarMiembroCommand(Long hogarId, Long idAdministrador, String nombre, String correo) {}
