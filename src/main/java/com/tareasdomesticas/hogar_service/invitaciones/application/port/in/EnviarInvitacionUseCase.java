package com.tareasdomesticas.hogar_service.invitaciones.application.port.in;

import com.tareasdomesticas.hogar_service.invitaciones.application.dto.InvitacionDTO;

public interface EnviarInvitacionUseCase {
    InvitacionDTO enviar(EnviarInvitacionCommand command);
}
