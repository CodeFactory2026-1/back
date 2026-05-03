package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.tareas.application.assembler.TareaListadoAssembler;
import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.CambiarEstadoCommand;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.CambiarEstadoTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.*;

public class CambiarEstadoTareaService implements CambiarEstadoTareaUseCase {

    private final TareaRepository tareaRepository;
    private final AsignacionSemanalRepository asignacionRepository;

    public CambiarEstadoTareaService(TareaRepository tareaRepository,
                                     AsignacionSemanalRepository asignacionRepository) {
        this.tareaRepository      = tareaRepository;
        this.asignacionRepository = asignacionRepository;
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

        return TareaListadoAssembler.toDTO(tarea, ast);
    }
}
