package com.tareasdomesticas.hogar_service.tareas.application.dto;

import java.time.LocalDateTime;


public class TareaListadoDTO {
    private Long idTarea;
    private Long idHogar;
    private String nombre;
    private String descripcion;
    private String dificultad;
    private String prioridad;
    private String estado;
    private UsuarioAsignadoDTO usuarioAsignado;
    private LocalDateTime fechaLimite;

    public TareaListadoDTO(Long idTarea, Long idHogar, String nombre, String descripcion,
            String dificultad, String prioridad,
            String estado, UsuarioAsignadoDTO usuarioAsignado, LocalDateTime fechaLimite) {
        this.idTarea = idTarea;
        this.idHogar = idHogar;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dificultad = dificultad;
        this.prioridad = prioridad;
        this.estado = estado;
        this.usuarioAsignado = usuarioAsignado;
        this.fechaLimite = fechaLimite;
    }

    public Long getIdTarea() { return idTarea; }
    public Long getIdHogar() { return idHogar; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getDificultad() { return dificultad; }
    public String getPrioridad() { return prioridad; }
    public String getEstado() { return estado; }
    public UsuarioAsignadoDTO getUsuarioAsignado() { return usuarioAsignado; }
    public LocalDateTime getFechaLimite() { return fechaLimite; }
}