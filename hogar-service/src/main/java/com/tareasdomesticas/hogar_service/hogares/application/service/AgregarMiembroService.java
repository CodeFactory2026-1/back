package com.tareasdomesticas.hogar_service.hogares.application.service;

import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.hogares.application.dto.MiembroDTO;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.AgregarMiembroCommand;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.AgregarMiembroUseCase;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;

public class AgregarMiembroService implements AgregarMiembroUseCase {

    private final HogarRepository hogarRepository;

    public AgregarMiembroService(HogarRepository hogarRepository) {
        this.hogarRepository = hogarRepository;
    }

    @Override
    public MiembroDTO agregarMiembro(AgregarMiembroCommand command) {
        Hogar hogar = hogarRepository.buscarPorId(command.hogarId())
                .orElseThrow(() -> new IllegalArgumentException("El hogar no existe."));

        if (!hogar.getAdministrador().getIdUsuario().equals(command.idAdministrador()))
            throw new IllegalStateException("Solo el administrador puede agregar miembros.");

        Usuario nuevo = new Usuario(null, command.nombre(), command.correo());
        hogar.agregarMiembro(nuevo);

        Hogar guardado = hogarRepository.guardar(hogar);

        Usuario agregado = guardado.getUsuarios().stream()
                .filter(u -> u.getCorreoUsuario().equalsIgnoreCase(command.correo()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró el miembro recién agregado."));

        return new MiembroDTO(
                agregado.getIdUsuario(),
                agregado.getNombreUsuario(),
                agregado.getCorreoUsuario(),
                agregado.getRolUsuario().name());
    }
}
