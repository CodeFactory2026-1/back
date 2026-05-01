package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.tareas.application.assembler.TareaListadoAssembler;
import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.EditarTareaCommand;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.EditarTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.AsignacionSemanalRepository;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

public class EditarTareaService implements EditarTareaUseCase {

    private final TareaRepository tareaRepository;
    private final AsignacionSemanalRepository asignacionSemanalRepository;

    public EditarTareaService(TareaRepository tareaRepository,
                              AsignacionSemanalRepository asignacionSemanalRepository) {
        this.tareaRepository              = tareaRepository;
        this.asignacionSemanalRepository  = asignacionSemanalRepository;
    }

    @Override
    public TareaListadoDTO editarTarea(EditarTareaCommand command) {
        if (command.nuevaDificultad() == null || command.nuevaDificultad().isBlank())
            throw new IllegalArgumentException("La dificultad es obligatoria.");

        DificultadTarea dificultad;
        try {
            dificultad = DificultadTarea.valueOf(command.nuevaDificultad().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dificultad no válida: " + command.nuevaDificultad());
        }

        Tarea tarea = tareaRepository.buscarPorId(command.idTarea())
                .orElseThrow(() -> new IllegalArgumentException("La tarea no existe."));

        asignacionSemanalRepository
                .buscarAsignacionActivaDeTarea(tarea.getIdTarea(), tarea.getIdHogar())
                .ifPresent(ast -> {
                    if (ast.getEstado() == EstadoTarea.ASIGNADO
                            || ast.getEstado() == EstadoTarea.EN_PROCESO)
                        throw new IllegalStateException(
                                "No se puede editar una tarea asignada o en proceso.");
                });

        tarea.editar(command.nuevoNombre(), command.nuevaDescripcion(),
                     dificultad, command.nuevaFecha());

        Tarea actualizada = tareaRepository.actualizar(tarea);
        AsignacionSemanalTarea ast = asignacionSemanalRepository
                .buscarAsignacionActivaDeTarea(actualizada.getIdTarea(), actualizada.getIdHogar())
                .orElse(null);

        return TareaListadoAssembler.toDTO(actualizada, ast);
    }
}
