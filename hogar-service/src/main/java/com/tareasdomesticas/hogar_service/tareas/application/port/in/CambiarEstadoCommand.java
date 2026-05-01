package com.tareasdomesticas.hogar_service.tareas.application.port.in;

public record CambiarEstadoCommand(Long idTarea, String nuevoEstado) {}
