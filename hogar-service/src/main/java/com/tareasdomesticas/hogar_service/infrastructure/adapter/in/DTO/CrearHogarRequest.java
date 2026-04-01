package com.tareasdomesticas.hogar_service.infrastructure.adapter.in.DTO;

public class CrearHogarRequest {
    private Integer usuarioId;
    private String nombreUsuario;
    private String correoUsuario;
    private String nombreHogar;
    private String descripcion;

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getNombreHogar() {
        return nombreHogar;
    }

    public void setNombreHogar(String nombreHogar) {
        this.nombreHogar = nombreHogar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}