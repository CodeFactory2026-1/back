package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;

public class CambiarEstadoRequest {
    @NotBlank(message = "El nuevo estado es obligatorio")
    private String nuevoEstado;
    public String getNuevoEstado()       { return nuevoEstado; }
    public void setNuevoEstado(String v) { this.nuevoEstado = v; }
}
