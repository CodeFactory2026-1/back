package com.tareasdomesticas.hogar_service.tareas.application.port.in;

import java.time.LocalDateTime;

import com.tareasdomesticas.hogar_service.tareas.domain.model.Tarea;

public interface CrearTareaUseCase {
    Tarea crearTarea(Long idHogar, String nombre, String foto, LocalDateTime fechaLimite, String dificultad, String prioridad);

}
