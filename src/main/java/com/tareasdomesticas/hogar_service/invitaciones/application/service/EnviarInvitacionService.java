package com.tareasdomesticas.hogar_service.invitaciones.application.service;

import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.invitaciones.application.dto.InvitacionDTO;
import com.tareasdomesticas.hogar_service.invitaciones.application.port.in.EnviarInvitacionCommand;
import com.tareasdomesticas.hogar_service.invitaciones.application.port.in.EnviarInvitacionUseCase;
import com.tareasdomesticas.hogar_service.invitaciones.domain.model.Invitacion;
import com.tareasdomesticas.hogar_service.invitaciones.domain.port.out.InvitacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnviarInvitacionService implements EnviarInvitacionUseCase {

    private static final Logger log = LoggerFactory.getLogger(EnviarInvitacionService.class);

    private final InvitacionRepository invitacionRepo;
    private final HogarRepository      hogarRepo;

    public EnviarInvitacionService(InvitacionRepository invitacionRepo,
                                   HogarRepository hogarRepo) {
        this.invitacionRepo = invitacionRepo;
        this.hogarRepo      = hogarRepo;
    }

    @Override
    public InvitacionDTO enviar(EnviarInvitacionCommand cmd) {

        Hogar hogar = hogarRepo.buscarPorId(cmd.idHogar())
                .orElseThrow(() -> new IllegalArgumentException("El hogar no existe."));

        if (!hogar.getAdministrador().getIdUsuario().equals(cmd.idAdministrador()))
            throw new IllegalStateException("No tiene permisos para invitar miembros.");

        // HU6: "Este correo ya es miembro del hogar."
        if (invitacionRepo.existeMiembroConCorreo(cmd.correo(), cmd.idHogar()))
            throw new IllegalStateException("Este correo ya es miembro del hogar.");

        // HU6: "Ya existe una invitación pendiente para este correo."
        invitacionRepo.buscarPendientePorCorreoYHogar(cmd.correo(), cmd.idHogar())
                .ifPresent(i -> {
                    throw new IllegalStateException("Ya existe una invitación pendiente para este correo.");
                });

        Invitacion inv     = new Invitacion(null, cmd.idHogar(), cmd.nombre(), cmd.correo());
        Invitacion guardada = invitacionRepo.guardar(inv);

        log.info("Invitación enviada: hogar={}, correo={}", cmd.idHogar(), cmd.correo());
        return toDTO(guardada);
    }

    private InvitacionDTO toDTO(Invitacion i) {
        return new InvitacionDTO(i.getId(), i.getIdHogar(), i.getNombreInvitado(),
                i.getCorreoInvitado(), i.getEstado().name(), i.getFechaEnvio());
    }
}
