package com.tareasdomesticas.hogar_service.tareas.application.dto;



public class TareaExcedenteDTO {
    private Long idTarea;
    private String nombre;
    private String dificultad;
    private String estado;

    public TareaExcedenteDTO(Long idTarea, String nombre, String dificultad, String estado) {
        this.idTarea = idTarea;
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.estado = estado;
    }

    public Long getIdTarea() { return idTarea; }
    public String getNombre() { return nombre; }
    public String getDificultad() { return dificultad; }
    public String getEstado() { return estado; }
}
