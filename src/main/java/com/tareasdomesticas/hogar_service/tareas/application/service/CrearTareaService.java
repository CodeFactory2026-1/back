package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.common.application.port.out.ResolverNombreUsuarioPort;
import com.tareasdomesticas.hogar_service.historial.application.port.in.RegistrarAccionHistorialUseCase;
import com.tareasdomesticas.hogar_service.historial.domain.model.TipoAccion;
import com.tareasdomesticas.hogar_service.tareas.application.dto.CrearTareaResultDTO;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.CrearTareaCommand;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.CrearTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

public class CrearTareaService implements CrearTareaUseCase {

    private final TareaRepository tareaRepository;
    private final RegistrarAccionHistorialUseCase historial;
    private final ResolverNombreUsuarioPort resolverNombre;

    public CrearTareaService(TareaRepository tareaRepository,
            RegistrarAccionHistorialUseCase historial,
            ResolverNombreUsuarioPort resolverNombre) {
        this.tareaRepository = tareaRepository;
        this.historial = historial;
        this.resolverNombre = resolverNombre;
    }

    @Override
    public CrearTareaResultDTO crearTarea(CrearTareaCommand command) {
        try {
            DificultadTarea dif = parsearDificultad(command.dificultad());
            PrioridadTarea pri = parsearPrioridad(command.prioridad());

            if (command.nombreTarea() != null && command.fechaLimite() != null
                    && command.idHogar() != null
                    && tareaRepository.existeTareaConMismoNombreEnSemana(
                            command.nombreTarea(), command.fechaLimite(), command.idHogar()))
                throw new IllegalArgumentException(
                        "Ya existe una tarea con ese nombre en el hogar. " +
                                "Por favor elige un nombre diferente.");

            Tarea tarea = new Tarea(null, command.idHogar(), command.idUsuarioCreador(),
                    command.nombreTarea(), command.descripcionTarea(), command.fotoTarea(),
                    command.fechaLimite(), dif, pri);

            Tarea guardada = tareaRepository.guardar(tarea);

            // Actor = administrador que creó la tarea
            String nombreAdmin = resolverNombre.resolverNombre(guardada.getIdUsuarioCreador());
            String detalle = "Tarea creada por " + nombreAdmin;
            historial.registrar(guardada.getIdHogar(), guardada.getIdTarea(),
                    guardada.getNombreTarea(), TipoAccion.TAREA_CREADA,
                    guardada.getIdUsuarioCreador(), nombreAdmin, detalle);

            return toDTO(guardada);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Algo salió mal, inténtelo de nuevo.", e);
        }
    }

    private DificultadTarea parsearDificultad(String v) {
        if (v == null || v.isBlank())
            throw new IllegalArgumentException("La dificultad es obligatoria.");
        try {
            return DificultadTarea.valueOf(v.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dificultad inválida: " + v);
        }
    }

    private PrioridadTarea parsearPrioridad(String v) {
        if (v == null || v.isBlank())
            throw new IllegalArgumentException("La prioridad es obligatoria.");
        try {
            return PrioridadTarea.valueOf(v.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Prioridad inválida: " + v);
        }
    }

    private CrearTareaResultDTO toDTO(Tarea t) {
        return new CrearTareaResultDTO(
                t.getIdTarea(), t.getNombreTarea(), t.getFotoTarea(), t.getFechaLimite(),
                t.getDificultad() != null ? t.getDificultad().name() : null,
                t.getPrioridad() != null ? t.getPrioridad().name() : null,
                "PENDIENTE");
    }
}
