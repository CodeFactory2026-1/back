package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in.DTO;


import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CrearTareaRequest {
    @NotNull(message = "El id del hogar es obligatorio")
    private Long idHogar;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    private String foto;
    @NotNull(message = "La fecha límite es obligatoria")
    private LocalDateTime fechaLimite;
    @NotBlank(message = "La dificultad es obligatoria")
    private String dificultad;
    @NotBlank(message = "La prioridad es obligatoria")
    private String prioridad;

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }
    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }
    public void setFechaLimite(LocalDateTime fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
    public String getDificultad() {
        return dificultad;
    }
    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }
    public String getPrioridad() {
        return prioridad;
    }
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
    public Long getHogarId() {
        return idHogar;
    }
    public void setHogarId(Long hogarId) {
        this.idHogar = hogarId;
    }
}
