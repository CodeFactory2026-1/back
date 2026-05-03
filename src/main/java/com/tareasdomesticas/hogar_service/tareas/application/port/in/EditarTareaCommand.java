package com.tareasdomesticas.hogar_service.tareas.application.port.in;

import java.time.LocalDateTime;

public record EditarTareaCommand(
    Long idTarea,
    String nuevoNombre,
    String nuevaDescripcion,
    String nuevaDificultad,
    LocalDateTime nuevaFecha
) {}
