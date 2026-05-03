package com.tareasdomesticas.hogar_service.tareas.application.dto;



public record FiltroTareasDTO(
    Long hogarId,
    String estado,
    Long idUsuario,
    String prioridad,
    String dificultad,
    String nombre
) {}
