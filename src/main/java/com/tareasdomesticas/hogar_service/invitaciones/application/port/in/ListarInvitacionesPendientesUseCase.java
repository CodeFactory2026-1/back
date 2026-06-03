package com.tareasdomesticas.hogar_service.invitaciones.application.port.in;

import com.tareasdomesticas.hogar_service.invitaciones.application.dto.InvitacionDTO;
import java.util.List;

public interface ListarInvitacionesPendientesUseCase {
    List<InvitacionDTO> listarParaUsuario(String correoUsuario);
}
