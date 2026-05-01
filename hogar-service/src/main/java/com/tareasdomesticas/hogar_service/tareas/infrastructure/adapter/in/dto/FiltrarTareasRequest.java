package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in.dto;

public class FiltrarTareasRequest {
    private String estado;
    private Long idUsuario;
    private String prioridad;
    private String dificultad;
    private String nombre;

    public String getEstado()        { return estado; }
    public void setEstado(String v)  { this.estado = v; }
    public Long getIdUsuario()       { return idUsuario; }
    public void setIdUsuario(Long v) { this.idUsuario = v; }
    public String getPrioridad()     { return prioridad; }
    public void setPrioridad(String v){ this.prioridad = v; }
    public String getDificultad()    { return dificultad; }
    public void setDificultad(String v){ this.dificultad = v; }
    public String getNombre()        { return nombre; }
    public void setNombre(String v)  { this.nombre = v; }
}
