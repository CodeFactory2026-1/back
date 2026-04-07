package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in.DTO;

import java.time.LocalDateTime;

public class CrearTareaResponse {
    private String mensaje;
    private Long id;
    private String nombre;
    private String foto;
    private LocalDateTime fechaLimite;
    private String dificultad;
    private String prioridad;
    private String estado;

    public CrearTareaResponse(String mensaje, Long id, String nombre, String foto, LocalDateTime fechaLimite, String dificultad,
            String prioridad, String estado) {
        this.mensaje = mensaje;
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.fechaLimite = fechaLimite;
        this.dificultad = dificultad;
        this.prioridad = prioridad;
        this.estado = estado;
    }


    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFoto() {
        return foto;
    }

    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }

    public String getDificultad() {
        return dificultad;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getEstado() {
        return estado;
    }
    public String getMensaje() {
        return mensaje;
    }
}
