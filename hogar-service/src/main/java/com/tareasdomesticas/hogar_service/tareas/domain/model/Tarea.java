package com.tareasdomesticas.hogar_service.tareas.domain.model;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;

public class Tarea {

    private Long idTarea;
    private Long idHogar;
    private String nombreTarea;
    private String descripcionTarea;
    private String fotoTarea;
    private LocalDateTime fechaLimite;
    private DificultadTarea dificultad;
    private PrioridadTarea prioridad;

    public Tarea(Long idTarea, Long idHogar, String nombreTarea, String descripcionTarea,
            String fotoTarea, LocalDateTime fechaLimite,
            DificultadTarea dificultad, PrioridadTarea prioridad) {
        validarIdHogar(idHogar);
        validarNombre(nombreTarea);
        validarDescripcion(descripcionTarea);
        validarFecha(fechaLimite);
        validarFoto(fotoTarea);
        if (dificultad == null)
            throw new IllegalArgumentException("La dificultad es obligatoria.");
        if (prioridad == null)
            throw new IllegalArgumentException("La prioridad es obligatoria.");

        this.idTarea = idTarea;
        this.idHogar = idHogar;
        this.nombreTarea = nombreTarea;
        this.descripcionTarea = descripcionTarea;
        this.fotoTarea = fotoTarea;
        this.fechaLimite = fechaLimite;
        this.dificultad = dificultad;
        this.prioridad = prioridad;
    }

    private Tarea() {
    }

    public static Tarea reconstruir(Long idTarea, Long idHogar, String nombreTarea,
            String descripcionTarea, String fotoTarea, LocalDateTime fechaLimite,
            DificultadTarea dificultad, PrioridadTarea prioridad) {
        if (idHogar == null)
            throw new IllegalArgumentException("El idHogar es obligatorio.");
        if (nombreTarea == null || nombreTarea.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (dificultad == null)
            throw new IllegalArgumentException("La dificultad es obligatoria.");
        if (prioridad == null)
            throw new IllegalArgumentException("La prioridad es obligatoria.");
        Tarea t = new Tarea();
        t.idTarea = idTarea;
        t.idHogar = idHogar;
        t.nombreTarea = nombreTarea;
        t.descripcionTarea = descripcionTarea;
        t.fotoTarea = fotoTarea;
        t.fechaLimite = fechaLimite;
        t.dificultad = dificultad;
        t.prioridad = prioridad;
        return t;
    }
    private void validarIdHogar(Long v) {
        if (v == null)
            throw new IllegalArgumentException("El idHogar es obligatorio.");
    }

    private void validarNombre(String v) {
        if (v == null || v.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (v.length() < 3 || v.length() > 30)
            throw new IllegalArgumentException("El nombre debe tener entre 3 y 30 caracteres.");
    }

    private void validarDescripcion(String v) {
        if (v != null && v.length() > 200)
            throw new IllegalArgumentException(
                    "La descripción debe cumplir con el rango de caracteres establecidos (máximo 200).");
    }

    private void validarFecha(LocalDateTime v) {
        if (v == null)
            throw new IllegalArgumentException("La fecha límite es obligatoria.");
        if (v.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("La fecha límite no puede ser anterior al momento actual.");
    }

    private void validarFoto(String v) {
        if (v != null && !v.isBlank()) {
            String lower = v.toLowerCase();
            if (!lower.endsWith(".jpg") && !lower.endsWith(".jpeg"))
                throw new IllegalArgumentException("Formato no permitido, debe subir JPG.");
        }
    }

    public void editar(String nuevoNombre, String nuevaDescripcion,
            DificultadTarea nuevaDificultad, LocalDateTime nuevaFecha) {
        validarNombre(nuevoNombre);
        validarFecha(nuevaFecha);
        if (nuevaDificultad == null)
            throw new IllegalArgumentException("La dificultad es obligatoria.");
        this.nombreTarea = nuevoNombre;
        this.descripcionTarea = nuevaDescripcion;
        this.dificultad = nuevaDificultad;
        this.fechaLimite = nuevaFecha;
    }

    public int getSemana() {
        return fechaLimite.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }
    public Long getIdTarea() {
        return idTarea;
    }

    public Long getIdHogar() {
        return idHogar;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public String getDescripcionTarea() {
        return descripcionTarea;
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
}