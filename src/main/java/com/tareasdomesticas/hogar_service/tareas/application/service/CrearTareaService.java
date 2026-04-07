package com.tareasdomesticas.hogar_service.tareas.application.service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import com.tareasdomesticas.hogar_service.tareas.application.port.in.CrearTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

public class CrearTareaService implements CrearTareaUseCase {

    private final TareaRepository tareaRepository;
    private final AtomicLong idGenerator = new AtomicLong(1);

   public CrearTareaService(TareaRepository tareaRepository) {
       this.tareaRepository = tareaRepository;
   }

   @Override
   public Tarea crearTarea(Long idHogar, String nombre, String foto, LocalDateTime fechaLimite, String dificultad, String prioridad) {
        if (idHogar == null) {
            throw new IllegalArgumentException("El idHogar es obligatorio.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (fechaLimite == null) {
            throw new IllegalArgumentException("La fecha límite es obligatoria");
        }
        if (dificultad == null || dificultad.isBlank()) {
            throw new IllegalArgumentException("La dificultad es obligatoria");
        }
        if (prioridad == null || prioridad.isBlank()) {
            throw new IllegalArgumentException("La prioridad es obligatoria");
        }
        if (foto != null && !foto.isBlank() && !foto.toLowerCase().endsWith(".jpg")) {
            throw new IllegalArgumentException("Formato no permitido, debe subir JPG.");
        }
        if (tareaRepository.existeTareaConMismoNombreEnSemana(nombre, fechaLimite, idHogar)) {
            throw new IllegalArgumentException("Ya existe una tarea con el mismo nombre en esta semana.");
        }
        try {
            DificultadTarea dif = DificultadTarea.valueOf(dificultad.toUpperCase());
            PrioridadTarea pri = PrioridadTarea.valueOf(prioridad.toUpperCase());
            Tarea tarea = new Tarea(
                idGenerator.getAndIncrement(),
                idHogar,
                nombre,
                foto,
                fechaLimite,
                dif,
                pri
            );
            return tareaRepository.guardar(tarea);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor inválido: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Algo salió mal, inténtelo de nuevo.", e);
        }
}
}