package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.common.application.port.out.ResolverNombreUsuarioPort;
import com.tareasdomesticas.hogar_service.historial.application.port.in.RegistrarAccionHistorialUseCase;
import com.tareasdomesticas.hogar_service.historial.domain.model.TipoAccion;
import com.tareasdomesticas.hogar_service.tareas.application.assembler.TareaListadoAssembler;
import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.CambiarEstadoCommand;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.CambiarEstadoTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.*;

public class CambiarEstadoTareaService implements CambiarEstadoTareaUseCase {

    private final TareaRepository tareaRepository;
    private final AsignacionSemanalRepository asignacionRepository;
    private final RegistrarAccionHistorialUseCase historial;
    private final ResolverNombreUsuarioPort resolverNombre;

    public CambiarEstadoTareaService(TareaRepository tareaRepository,
            AsignacionSemanalRepository asignacionRepository,
            RegistrarAccionHistorialUseCase historial,
            ResolverNombreUsuarioPort resolverNombre) {
        this.tareaRepository = tareaRepository;
        this.asignacionRepository = asignacionRepository;
        this.historial = historial;
        this.resolverNombre = resolverNombre;
    }

    @Override
    public TareaListadoDTO cambiarEstado(CambiarEstadoCommand command) {
        EstadoTarea nuevoEstado;
        try {
            nuevoEstado = EstadoTarea.valueOf(command.nuevoEstado().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado no válido: " + command.nuevoEstado());
        }

        Tarea tarea = tareaRepository.buscarPorId(command.idTarea())
                .orElseThrow(() -> new IllegalArgumentException("La tarea no existe."));

        AsignacionSemanalTarea ast = asignacionRepository
                .buscarAsignacionActivaDeTarea(tarea.getIdTarea(), tarea.getIdHogar())
                .orElseThrow(() -> new IllegalStateException(
                        "La tarea no tiene una asignación activa en la semana actual."));

        ast.cambiarEstado(nuevoEstado);
        asignacionRepository.actualizarAsignacionTarea(ast);

        // Actor = miembro asignado a la tarea (quien ejecuta el cambio de estado)
        Long idMiembro = ast.getIdUsuarioAsignado() != null
                ? ast.getIdUsuarioAsignado()
                : tarea.getIdUsuarioCreador();
        String nombreMiembro = resolverNombre.resolverNombre(idMiembro);

        String detalleEstado = "Estado cambiado a " + nuevoEstado.name();
        if (nuevoEstado == EstadoTarea.FINALIZADO) {
            detalleEstado += " por " + nombreMiembro;
        }
        historial.registrar(tarea.getIdHogar(), tarea.getIdTarea(),
                tarea.getNombreTarea(), TipoAccion.TAREA_ESTADO_CAMBIADO,
                idMiembro, nombreMiembro, detalleEstado);

        return TareaListadoAssembler.toDTO(tarea, ast);
    }
}
