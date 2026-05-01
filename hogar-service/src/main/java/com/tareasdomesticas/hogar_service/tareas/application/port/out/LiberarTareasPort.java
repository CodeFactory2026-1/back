package com.tareasdomesticas.hogar_service.tareas.application.port.out;

public interface LiberarTareasPort {
    void liberarTareasDeUsuario(Long idUsuario, Long hogarId);
}
