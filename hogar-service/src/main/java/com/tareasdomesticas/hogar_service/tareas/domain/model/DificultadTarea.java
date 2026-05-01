package com.tareasdomesticas.hogar_service.tareas.domain.model;

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
