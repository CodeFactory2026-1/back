package com.tareasdomesticas.hogar_service.tareas.application.dto;
 
import java.time.LocalDateTime;
 
public class CrearTareaResultDTO {
 
    private final Long idTarea;
    private final String nombreTarea;
    private final String fotoTarea;
    private final LocalDateTime fechaLimite;
    private final String dificultad;
    private final String prioridad;
    private final String estado;
 
    public CrearTareaResultDTO(Long idTarea, String nombreTarea, String fotoTarea,
            LocalDateTime fechaLimite, String dificultad, String prioridad, String estado) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
        this.fotoTarea = fotoTarea;
        this.fechaLimite = fechaLimite;
        this.dificultad = dificultad;
        this.prioridad = prioridad;
        this.estado = estado;
    }
 
    public Long getIdTarea() { return idTarea; }
    public String getNombreTarea() { return nombreTarea; }
    public String getFotoTarea() { return fotoTarea; }
    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public String getDificultad() { return dificultad; }
    public String getPrioridad() { return prioridad; }
    public String getEstado() { return estado; }
}