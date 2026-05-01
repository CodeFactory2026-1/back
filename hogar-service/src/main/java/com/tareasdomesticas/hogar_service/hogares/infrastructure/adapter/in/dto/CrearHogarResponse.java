package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in.dto;

public class CrearHogarResponse {
    private final Long idHogar;
    private final String nombreHogar;
    private final String descripcionHogar;
    private final Long usuarioId;
    private final String mensaje;

    public CrearHogarResponse(Long idHogar, String nombreHogar, String descripcionHogar,
            Long usuarioId, String mensaje) {
        this.idHogar          = idHogar;
        this.nombreHogar      = nombreHogar;
        this.descripcionHogar = descripcionHogar;
        this.usuarioId        = usuarioId;
        this.mensaje          = mensaje;
    }

    public Long getIdHogar()           { return idHogar; }
    public String getNombreHogar()     { return nombreHogar; }
    public String getDescripcionHogar(){ return descripcionHogar; }
    public Long getUsuarioId()         { return usuarioId; }
    public String getMensaje()         { return mensaje; }
}
