package com.tareasdomesticas.hogar_service.tareas.application.dto;

public class TareaAsignadaDTO {
    private Long   idTarea;
    private String nombre;
    private String dificultad;
    private Long   usuarioAsignado;
    private String nombreUsuarioAsignado;
    private String estado;

    public TareaAsignadaDTO(Long idTarea, String nombre, String dificultad,
                             Long usuarioAsignado, String nombreUsuarioAsignado, String estado) {
        this.idTarea              = idTarea;
        this.nombre               = nombre;
        this.dificultad           = dificultad;
        this.usuarioAsignado      = usuarioAsignado;
        this.nombreUsuarioAsignado = nombreUsuarioAsignado;
        this.estado               = estado;
    }

    public Long   getIdTarea()               { return idTarea; }
    public String getNombre()                { return nombre; }
    public String getDificultad()            { return dificultad; }
    public Long   getUsuarioAsignado()       { return usuarioAsignado; }
    public String getNombreUsuarioAsignado() { return nombreUsuarioAsignado; }
    public String getEstado()                { return estado; }
}
