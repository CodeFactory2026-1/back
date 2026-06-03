package com.tareasdomesticas.hogar_service.hogares.application.service;

import com.tareasdomesticas.hogar_service.common.application.port.out.ResolverNombreUsuarioPort;
import com.tareasdomesticas.hogar_service.historial.application.port.in.RegistrarAccionHistorialUseCase;
import com.tareasdomesticas.hogar_service.historial.domain.model.TipoAccion;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.EliminarMiembroCommand;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.EliminarMiembroUseCase;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.tareas.application.port.out.LiberarTareasPort;

public class EliminarMiembroService implements EliminarMiembroUseCase {

    private final HogarRepository hogarRepository;
    private final LiberarTareasPort liberarTareasPort;
    private final RegistrarAccionHistorialUseCase historial;
    private final ResolverNombreUsuarioPort resolverNombre;

    public EliminarMiembroService(HogarRepository hogarRepository,
            LiberarTareasPort liberarTareasPort,
            RegistrarAccionHistorialUseCase historial,
            ResolverNombreUsuarioPort resolverNombre) {
        this.hogarRepository = hogarRepository;
        this.liberarTareasPort = liberarTareasPort;
        this.historial = historial;
        this.resolverNombre = resolverNombre;
    }

    @Override
    public void eliminarMiembro(EliminarMiembroCommand command) {
        Hogar hogar = hogarRepository.buscarPorId(command.hogarId())
                .orElseThrow(() -> new IllegalArgumentException("El hogar no existe."));

        if (!hogar.getAdministrador().getIdUsuario().equals(command.idAdministrador()))
            throw new IllegalStateException("Solo el administrador puede eliminar miembros.");

        if (hogar.getAdministrador().getIdUsuario().equals(command.idMiembro()))
            throw new IllegalStateException(
                    "Acción no permitida. No puedes eliminar al administrador del hogar.");

        hogar.eliminarMiembro(command.idMiembro());
        hogarRepository.guardar(hogar);
        liberarTareasPort.liberarTareasDeUsuario(command.idMiembro(), command.hogarId());

        // Actor = administrador que realizó la eliminación
        String nombreAdmin = resolverNombre.resolverNombre(command.idAdministrador());
        String nombreMiembro = resolverNombre.resolverNombre(command.idMiembro());
        String detalle = "Miembro eliminado: " + nombreMiembro;
        historial.registrar(command.hogarId(), null, null,
                TipoAccion.MIEMBRO_ELIMINADO,
                command.idAdministrador(), nombreAdmin, detalle);
    }
}
