package com.tareasdomesticas.hogar_service.invitaciones.application.service;

import com.tareasdomesticas.hogar_service.invitaciones.application.dto.InvitacionDTO;
import com.tareasdomesticas.hogar_service.invitaciones.application.port.in.ListarInvitacionesPendientesUseCase;
import com.tareasdomesticas.hogar_service.invitaciones.domain.model.Invitacion;
import com.tareasdomesticas.hogar_service.invitaciones.domain.port.out.InvitacionRepository;

import java.util.List;

public class ListarInvitacionesPendientesService implements ListarInvitacionesPendientesUseCase {

    private final InvitacionRepository invitacionRepo;

    public ListarInvitacionesPendientesService(InvitacionRepository invitacionRepo) {
        this.invitacionRepo = invitacionRepo;
    }

    @Override
    public List<InvitacionDTO> listarParaUsuario(String correoUsuario) {
        if (correoUsuario == null || correoUsuario.isBlank())
            throw new IllegalArgumentException("El correo es obligatorio.");
        return invitacionRepo.listarPendientesPorCorreo(correoUsuario.trim().toLowerCase())
                .stream().map(this::toDTO).toList();
    }

    private InvitacionDTO toDTO(Invitacion i) {
        return new InvitacionDTO(i.getId(), i.getIdHogar(), i.getNombreInvitado(),
                i.getCorreoInvitado(), i.getEstado().name(), i.getFechaEnvio());
    }
}
