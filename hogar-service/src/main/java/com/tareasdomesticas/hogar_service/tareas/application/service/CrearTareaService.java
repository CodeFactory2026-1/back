package com.tareasdomesticas.hogar_service.tareas.application.service;

import java.time.LocalDateTime;

import com.tareasdomesticas.hogar_service.tareas.application.dto.CrearTareaResultDTO;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.CrearTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.DificultadTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.PrioridadTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.Tarea;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

public class CrearTareaService implements CrearTareaUseCase {

    private final TareaRepository tareaRepository;

    public CrearTareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public CrearTareaResultDTO crearTarea(Long idHogar, String nombre, String descripcion,
            String foto, LocalDateTime fechaLimite, String dificultad, String prioridad) {
        try {
            DificultadTarea dif = parsearDificultad(dificultad);
            PrioridadTarea pri = parsearPrioridad(prioridad);
            if (nombre != null && fechaLimite != null && idHogar != null
                    && tareaRepository.existeTareaConMismoNombreEnSemana(nombre, fechaLimite, idHogar)) {
                throw new IllegalArgumentException("Ya existe una tarea con el mismo nombre en esta semana.");
            }
            Tarea tarea = new Tarea(null, idHogar, nombre, descripcion, foto, fechaLimite, dif, pri);
            Tarea guardada = tareaRepository.guardar(tarea);

            return toDTO(guardada);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Algo salió mal, inténtelo de nuevo.", e);
        }
    }

    private DificultadTarea parsearDificultad(String valor) {
        if (valor == null || valor.isBlank())
            throw new IllegalArgumentException("La dificultad es obligatoria");
        try {
            return DificultadTarea.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dificultad inválida: " + valor);
        }
    }

    private PrioridadTarea parsearPrioridad(String valor) {
        if (valor == null || valor.isBlank())
            throw new IllegalArgumentException("La prioridad es obligatoria");
        try {
            return PrioridadTarea.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Prioridad inválida: " + valor);
        }
    }

    private CrearTareaResultDTO toDTO(Tarea t) {
        return new CrearTareaResultDTO(
                t.getIdTarea(),
                t.getNombreTarea(),
                t.getFotoTarea(),
                t.getFechaLimite(),
                t.getDificultad() != null ? t.getDificultad().name() : null,
                t.getPrioridad()  != null ? t.getPrioridad().name()  : null,
                "PENDIENTE");
    }
}