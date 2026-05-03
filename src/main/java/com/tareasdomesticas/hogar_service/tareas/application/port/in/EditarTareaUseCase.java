package com.tareasdomesticas.hogar_service.tareas.application.port.in;

import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;

public interface EditarTareaUseCase {
    TareaListadoDTO editarTarea(EditarTareaCommand command);
}
