package com.tareasdomesticas.hogar_service.tareas.application.port.in;
import com.tareasdomesticas.hogar_service.tareas.application.dto.CrearTareaResultDTO;

public interface CrearTareaUseCase {
    CrearTareaResultDTO crearTarea(CrearTareaCommand command);
}