package com.tareasdomesticas.hogar_service.hogares.application.service;

import com.tareasdomesticas.hogar_service.hogares.application.port.in.EliminarMiembroCommand;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.EliminarMiembroUseCase;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.tareas.application.port.out.LiberarTareasPort;

public class EliminarMiembroService implements EliminarMiembroUseCase {

    private final HogarRepository hogarRepository;
    private final LiberarTareasPort liberarTareasPort;

    public EliminarMiembroService(HogarRepository hogarRepository,
                                  LiberarTareasPort liberarTareasPort) {
        this.hogarRepository  = hogarRepository;
        this.liberarTareasPort = liberarTareasPort;
    }

    @Override
    public void eliminarMiembro(EliminarMiembroCommand command) {
        Hogar hogar = hogarRepository.buscarPorId(command.hogarId())
                .orElseThrow(() -> new IllegalArgumentException("El hogar no existe."));

        if (!hogar.getAdministrador().getIdUsuario().equals(command.idAdministrador()))
            throw new IllegalStateException("Solo el administrador puede eliminar miembros.");

        hogar.eliminarMiembro(command.idMiembro());
        hogarRepository.guardar(hogar);

        // Se pasa el hogarId directamente — no se busca por usuario porque
        // en este punto el miembro ya fue eliminado de la BD.
        liberarTareasPort.liberarTareasDeUsuario(command.idMiembro(), command.hogarId());
    }
}
