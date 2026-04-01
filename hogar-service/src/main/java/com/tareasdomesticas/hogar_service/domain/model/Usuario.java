package com.tareasdomesticas.hogar_service.domain.model;

public class Usuario {
    private Integer idUsuario;
    private String nombreUsuario;
    private String correoUsuario;
    private RolUsuario rolUsuario;

    public Usuario(Integer idUsuario, String nombreUsuario, String correoUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
        this.rolUsuario = RolUsuario.MIEMBRO;
    }

    public void convertirEnAdministrador() {
        this.rolUsuario = RolUsuario.ADMINISTRADOR;
    }

    public boolean esAdministrador() {
        return this.rolUsuario == RolUsuario.ADMINISTRADOR;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public RolUsuario getRolUsuario() {
        return rolUsuario;
    }

}
