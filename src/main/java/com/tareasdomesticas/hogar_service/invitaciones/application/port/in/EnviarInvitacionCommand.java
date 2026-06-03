package com.tareasdomesticas.hogar_service.invitaciones.application.port.in;

public record EnviarInvitacionCommand(Long idHogar, Long idAdministrador,
                                       String nombre, String correo) {}
