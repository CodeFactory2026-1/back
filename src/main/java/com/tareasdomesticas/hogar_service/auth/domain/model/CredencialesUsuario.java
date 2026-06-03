package com.tareasdomesticas.hogar_service.auth.domain.model;

import java.util.regex.Pattern;

public class CredencialesUsuario {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?!\\.)(?!.*\\.\\.)[a-zA-Z0-9_+\\-.]+@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}$");
    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MIN_NOMBRE_LENGTH = 3;

    private final Long   idUsuario;
    private final String nombre;
    private final String correo;
    /** Hash almacenado (BCrypt). Nunca se expone en respuestas. */
    private       String hashContrasena;

    /** Constructor de creación — valida todas las reglas de negocio. */
    public CredencialesUsuario(Long idUsuario, String nombre,
                               String correo, String hashContrasena) {
        validarNombre(nombre);
        validarCorreo(correo);
        if (hashContrasena == null || hashContrasena.isBlank())
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        this.idUsuario      = idUsuario;
        this.nombre         = nombre.trim();
        this.correo         = correo.trim().toLowerCase();
        this.hashContrasena = hashContrasena;
    }

    /** Constructor de reconstrucción desde persistencia (sin revalidar). */
    public static CredencialesUsuario reconstruir(Long idUsuario, String nombre,
                                                   String correo, String hashContrasena) {
        CredencialesUsuario c = new CredencialesUsuario(idUsuario, nombre, correo, hashContrasena);
        return c;
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (nombre.trim().length() < MIN_NOMBRE_LENGTH)
            throw new IllegalArgumentException(
                    "El nombre debe tener al menos " + MIN_NOMBRE_LENGTH + " caracteres.");
        if (nombre.trim().length() > 50)
            throw new IllegalArgumentException("El nombre no puede superar los 50 caracteres.");
    }

    private void validarCorreo(String correo) {
        if (correo == null || correo.isBlank())
            throw new IllegalArgumentException("El correo es obligatorio.");
        String c = correo.trim();
        if (c.length() > MAX_EMAIL_LENGTH)
            throw new IllegalArgumentException("El correo supera la longitud máxima permitida.");
        if (c.startsWith(".") || c.endsWith("."))
            throw new IllegalArgumentException("El correo no puede empezar ni terminar con punto.");
        if (!EMAIL_PATTERN.matcher(c).matches())
            throw new IllegalArgumentException("El formato del correo no es válido.");
    }

    public Long   getIdUsuario()      { return idUsuario; }
    public String getNombre()         { return nombre; }
    public String getCorreo()         { return correo; }
    public String getHashContrasena() { return hashContrasena; }
}
