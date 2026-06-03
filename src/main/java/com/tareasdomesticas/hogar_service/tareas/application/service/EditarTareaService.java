package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.common.application.port.out.ResolverNombreUsuarioPort;
import com.tareasdomesticas.hogar_service.historial.application.port.in.RegistrarAccionHistorialUseCase;
import com.tareasdomesticas.hogar_service.historial.domain.model.TipoAccion;
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
    private final RegistrarAccionHistorialUseCase historial;
    private final ResolverNombreUsuarioPort resolverNombre;

    public EditarTareaService(TareaRepository tareaRepository,
            AsignacionSemanalRepository asignacionSemanalRepository,
            RegistrarAccionHistorialUseCase historial,
            ResolverNombreUsuarioPort resolverNombre) {
        this.tareaRepository = tareaRepository;
        this.asignacionSemanalRepository = asignacionSemanalRepository;
        this.historial = historial;
        this.resolverNombre = resolverNombre;
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

        // Actor = administrador que ejecutó la edición (viene del request, no del
        // creador)
        String nombreAdmin = resolverNombre.resolverNombre(command.idUsuarioAdmin());
        String detalle = "Tarea editada por " + nombreAdmin;
        historial.registrar(actualizada.getIdHogar(), actualizada.getIdTarea(),
                actualizada.getNombreTarea(), TipoAccion.TAREA_EDITADA,
                command.idUsuarioAdmin(), nombreAdmin, detalle);

        AsignacionSemanalTarea ast = asignacionSemanalRepository
                .buscarAsignacionActivaDeTarea(actualizada.getIdTarea(), actualizada.getIdHogar())
                .orElse(null);

        return TareaListadoAssembler.toDTO(actualizada, ast);
    }
}
