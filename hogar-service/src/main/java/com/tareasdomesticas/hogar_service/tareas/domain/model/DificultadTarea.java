package com.tareasdomesticas.hogar_service.tareas.domain.model;

/**
 * Enum de dificultad de tarea.
 * Cada valor conoce su propio peso para el algoritmo de balanceo de carga.
 * Principio OCP: agregar un nuevo nivel no requiere modificar AsignarTareaService.
 */
public enum DificultadTarea {
    BAJA(1),
    MEDIA(2),
    ALTA(3);

    private final int peso;

    DificultadTarea(int peso) {
        this.peso = peso;
    }

    public int getPeso() {
        return peso;
    }
}
