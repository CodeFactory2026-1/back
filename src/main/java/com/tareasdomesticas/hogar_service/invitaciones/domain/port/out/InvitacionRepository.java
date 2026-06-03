package com.tareasdomesticas.hogar_service.invitaciones.domain.port.out;

import com.tareasdomesticas.hogar_service.invitaciones.domain.model.Invitacion;
import java.util.List;
import java.util.Optional;

public interface InvitacionRepository {
    Invitacion guardar(Invitacion invitacion);
    Optional<Invitacion> buscarPorId(Long id);
    Optional<Invitacion> buscarPendientePorCorreoYHogar(String correo, Long idHogar);
    List<Invitacion> listarPendientesPorCorreo(String correoInvitado);
    boolean existeMiembroConCorreo(String correo, Long idHogar);
}
