package com.tareasdomesticas.hogar_service.common.domain.model;

public class Usuario {

    private Long idUsuario;
    private String nombreUsuario;
    private String correoUsuario;
    private RolUsuario rolUsuario;
    private Long idHogar;

    public Usuario(Long idUsuario, String nombreUsuario, String correoUsuario) {
        if (nombreUsuario == null || nombreUsuario.isBlank())
            throw new IllegalArgumentException("El nombre del usuario es obligatorio.");
        if (nombreUsuario.length() > 50)
            throw new IllegalArgumentException("El nombre del usuario no puede superar los 50 caracteres.");
        if (correoUsuario == null || correoUsuario.isBlank())
            throw new IllegalArgumentException("El correo del usuario es obligatorio.");

        this.idUsuario     = idUsuario; 
        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
        this.rolUsuario    = RolUsuario.MIEMBRO;
    }

    public void convertirEnAdministrador() { this.rolUsuario = RolUsuario.ADMINISTRADOR; }
    public boolean esAdministrador()       { return this.rolUsuario == RolUsuario.ADMINISTRADOR; }

    public void asignarHogar(Long idHogar) { this.idHogar = idHogar; }

    // ── Getters ───────────────────────────────────────────────────────────
    public Long   getIdUsuario()      { return idUsuario; }
    public String getNombreUsuario()  { return nombreUsuario; }
    public String getCorreoUsuario()  { return correoUsuario; }
    public RolUsuario getRolUsuario() { return rolUsuario; }
    public Long   getIdHogar()        { return idHogar; }
}
