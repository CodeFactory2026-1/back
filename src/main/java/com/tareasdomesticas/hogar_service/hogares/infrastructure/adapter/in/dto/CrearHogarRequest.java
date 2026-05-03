package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CrearHogarRequest {

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El nombre del usuario es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "El correo del usuario es obligatorio")
    private String correoUsuario;

    @NotBlank(message = "El nombre del hogar es obligatorio")
    private String nombreHogar;

    private String descripcion;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
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