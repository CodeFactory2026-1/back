package com.tareasdomesticas.hogar_service.invitaciones.domain.model;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Agregado del módulo de invitaciones.
 * El DDL no persiste idAdministrador; la autorización (admin only)
 * se valida en EnviarInvitacionService antes de llegar aquí.
 */
public class Invitacion {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?!\\.)(?!.*\\.\\.)([a-zA-Z0-9_+\\-.]+)@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}$");
    private static final int MAX_EMAIL = 254;

    private final Long id;
    private final Long idHogar;
    private final String nombreInvitado;
    private final String correoInvitado;
    private EstadoInvitacion estado;
    private final LocalDateTime fechaEnvio;
    private LocalDateTime fechaRespuesta;

    public Invitacion(Long id, Long idHogar, String nombreInvitado, String correoInvitado) {
        if (idHogar == null)
            throw new IllegalArgumentException("El idHogar es obligatorio.");
        validarNombre(nombreInvitado);
        validarCorreo(correoInvitado);
        this.id = id;
        this.idHogar = idHogar;
        this.nombreInvitado = nombreInvitado.trim();
        this.correoInvitado = correoInvitado.trim().toLowerCase();
        this.estado = EstadoInvitacion.PENDIENTE;
        this.fechaEnvio = LocalDateTime.now();
    }

    /** Constructor de reconstrucción desde persistencia. */
    public static Invitacion reconstruir(Long id, Long idHogar,
            String nombreInvitado, String correoInvitado,
            EstadoInvitacion estado, LocalDateTime fechaEnvio, LocalDateTime fechaRespuesta) {
        Invitacion inv = new Invitacion(id, idHogar, nombreInvitado, correoInvitado);
        inv.estado = estado;
        inv.fechaRespuesta = fechaRespuesta;
        return inv;
    }

    public void aceptar() {

        if (this.estado != EstadoInvitacion.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo las invitaciones pendientes pueden aceptarse.");
        }

        this.estado = EstadoInvitacion.ACEPTADA;
        this.fechaRespuesta = LocalDateTime.now();
    }

    public void rechazar() {

        if (this.estado != EstadoInvitacion.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo las invitaciones pendientes pueden rechazarse.");
        }

        this.estado = EstadoInvitacion.RECHAZADA;
        this.fechaRespuesta = LocalDateTime.now();
    }

    // ── validaciones ──────────────────────────────────────────────────────────

    private void validarNombre(String n) {
        if (n == null || n.isBlank())
            throw new IllegalArgumentException("El nombre del invitado es obligatorio.");
    }

    private void validarCorreo(String c) {
        if (c == null || c.isBlank())
            throw new IllegalArgumentException("El correo es obligatorio.");
        String t = c.trim();
        if (t.contains(" "))
            throw new IllegalArgumentException("El formato del correo no es válido.");
        if (t.length() > MAX_EMAIL)
            throw new IllegalArgumentException("El correo supera la longitud máxima permitida.");
        if (t.startsWith(".") || t.endsWith("."))
            throw new IllegalArgumentException("El formato del correo no es válido.");
        if (!EMAIL_PATTERN.matcher(t).matches())
            throw new IllegalArgumentException("El formato del correo no es válido.");
    }

    public Long getId() {
        return id;
    }

    public Long getIdHogar() {
        return idHogar;
    }

    public String getNombreInvitado() {
        return nombreInvitado;
    }

    public String getCorreoInvitado() {
        return correoInvitado;
    }

    public EstadoInvitacion getEstado() {
        return estado;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }
}
