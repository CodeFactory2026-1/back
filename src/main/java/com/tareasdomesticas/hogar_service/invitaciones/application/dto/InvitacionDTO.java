package com.tareasdomesticas.hogar_service.invitaciones.application.dto;

import java.time.LocalDateTime;

public record InvitacionDTO(Long id, Long idHogar, String nombreInvitado,
                             String correoInvitado, String estado, LocalDateTime fechaEnvio) {}
