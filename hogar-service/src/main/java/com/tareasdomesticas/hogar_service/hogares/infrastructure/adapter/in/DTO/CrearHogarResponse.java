package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in.DTO;

public class CrearHogarResponse {
    private final Integer hogarId;
    private final String nombreHogar;
    private final String descripcion;
    private final Integer usuarioId;
    private final String mensaje;

    public CrearHogarResponse(Integer hogarId, String nombreHogar, String descripcion, Integer usuarioId,
            String mensaje) {
        this.hogarId = hogarId;
        this.nombreHogar = nombreHogar;
        this.descripcion = descripcion;
        this.usuarioId = usuarioId;
        this.mensaje = mensaje;
    }

    public Integer getHogarId() {
        return hogarId;
    }

    public String getNombreHogar() {
        return nombreHogar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public String getMensaje() {
        return mensaje;
    }
}
