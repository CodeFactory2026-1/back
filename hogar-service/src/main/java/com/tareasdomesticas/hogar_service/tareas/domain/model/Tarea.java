package com.tareasdomesticas.hogar_service.tareas.domain.model;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;

public class Tarea {
    private Long idTarea;
    private Long idHogar;
    private String nombreTarea;
    private String fotoTarea;
    private LocalDateTime fechaLimite;
    private DificultadTarea dificultad;
    private PrioridadTarea prioridad;
    private EstadoTarea estado;
    private Long idUsuarioAsignado;
    private boolean excedente = false;

    public Tarea(Long idTarea, Long idHogar, String nombreTarea, String fotoTarea, LocalDateTime fechaLimite, DificultadTarea dificultad,
            PrioridadTarea prioridad) {

        if (idHogar == null) {
            throw new IllegalArgumentException("El idHogar es obligatorio.");
        }
        if (nombreTarea == null || nombreTarea.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (nombreTarea.length() < 3 || nombreTarea.length() > 30) {
            throw new IllegalArgumentException("El nombre debe tener entre 3 y 30 caracteres.");
        }
        if (fechaLimite == null) {
            throw new IllegalArgumentException("La fecha límite es obligatoria.");
        }
        if (fechaLimite.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha límite no puede ser anterior al momento actual");
        }

        if (dificultad == null) {
            throw new IllegalArgumentException("La dificultad es obligatoria.");
        }
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad es obligatoria.");
        }

        this.idTarea = idTarea;
        this.idHogar = idHogar;
        this.nombreTarea = nombreTarea;
        this.fotoTarea = fotoTarea;
        this.fechaLimite = fechaLimite;
        this.dificultad = dificultad;
        this.prioridad = prioridad;
        this.estado = EstadoTarea.PENDIENTE;
    }
    public int getSemana() {
        return fechaLimite.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }
    public void asignarA(Long idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El usuario es obligatorio para asignar");
        }
        if (this.estado == EstadoTarea.ASIGNADO) {
            throw new IllegalStateException("La tarea ya está asignada");
        }
        this.idUsuarioAsignado = idUsuario;
        this.estado = EstadoTarea.ASIGNADO;
    }
    public Long getIdHogar() {
        return idHogar;
    }
    public Long getIdUsuarioAsignado() {
        return idUsuarioAsignado;
    }
    public boolean isExcedente() {
        return excedente;
    }
    public void marcarComoExcedente() {
        this.excedente = true;
    }
    public void marcarComoNoExcedente() {
        this.excedente = false;
    }
    public boolean esPendiente() {
        return this.estado == EstadoTarea.PENDIENTE;
    }
    public void marcarComoPendiente() {
        this.estado = EstadoTarea.PENDIENTE;
    }
    public Long getIdTarea() {
        return idTarea;
    }
    public String getNombreTarea() {
        return nombreTarea;
    }
    public String getFotoTarea() {
        return fotoTarea;
    }
    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }
    public DificultadTarea getDificultad() {
        return dificultad;
    }
    public PrioridadTarea getPrioridad() {
        return prioridad;
    }
    public EstadoTarea getEstado() {
        return estado;
    }


}