package com.tareasdomesticas.hogar_service.tareas.application.port.in;

public interface EliminarTareaUseCase {
    void eliminarTarea(Long idTarea, Long idUsuarioAdmin);
}
