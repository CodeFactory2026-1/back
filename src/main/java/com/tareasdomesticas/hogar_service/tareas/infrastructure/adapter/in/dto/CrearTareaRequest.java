package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

public class CrearTareaRequest {

    @NotNull(message = "El id del hogar es obligatorio")
    private Long idHogar;

    private Long idUsuarioCreador;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String foto;

    @Size(max = 200, message = "La descripción debe tener máximo 200 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha límite es obligatoria")
    @Future(message = "La fecha límite no puede ser anterior a la fecha actual")
    private LocalDateTime fechaLimite;

    @NotBlank(message = "La dificultad es obligatoria")
    private String dificultad;

    @NotBlank(message = "La prioridad es obligatoria")
    private String prioridad;

    public Long getIdHogar() {
        return idHogar;
    }

    public void setIdHogar(Long v) {
        this.idHogar = v;
    }

    public Long getIdUsuarioCreador() {
        return idUsuarioCreador;
    }

    public void setIdUsuarioCreador(Long v) {
        this.idUsuarioCreador = v;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String v) {
        this.nombre = v;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String v) {
        this.foto = v;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String v) {
        this.descripcion = v;
    }

    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDateTime v) {
        this.fechaLimite = v;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String v) {
        this.dificultad = v;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String v) {
        this.prioridad = v;
    }
}
