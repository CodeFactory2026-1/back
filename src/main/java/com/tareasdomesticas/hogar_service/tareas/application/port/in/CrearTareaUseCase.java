package com.tareasdomesticas.hogar_service.tareas.application.port.in;
import java.time.LocalDateTime;
import com.tareasdomesticas.hogar_service.tareas.application.dto.CrearTareaResultDTO;

public interface CrearTareaUseCase {
    CrearTareaResultDTO crearTarea(Long idHogar, String nombre, String descripcion,
            String foto, LocalDateTime fechaLimite, String dificultad, String prioridad);
}