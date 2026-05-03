package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EditarTareaRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    private String descripcion;
    @NotBlank(message = "La dificultad es obligatoria")
    private String dificultad;
    @NotNull(message = "La fecha límite es obligatoria")
    private LocalDateTime fechaLimite;

    public String getNombre()          { return nombre; }
    public void setNombre(String v)    { this.nombre = v; }
    public String getDescripcion()     { return descripcion; }
    public void setDescripcion(String v){ this.descripcion = v; }
    public String getDificultad()      { return dificultad; }
    public void setDificultad(String v){ this.dificultad = v; }
    public LocalDateTime getFechaLimite()      { return fechaLimite; }
    public void setFechaLimite(LocalDateTime v){ this.fechaLimite = v; }
}
