package com.tareasdomesticas.hogar_service.auth.domain.model;

import java.util.regex.Pattern;

public class CuentaUsuario {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?!\\.)(?!.*\\.\\.)[a-zA-Z0-9_+\\-.]+@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}$");
    private static final int MAX_EMAIL  = 254;
    private static final int MIN_NOMBRE = 3;

    private final Long   id;
    private final String nombre;
    private final String correo;
    private final String hashContrasena;
    private final Long   idHogar;
    /** "ADMINISTRADOR" o "MIEMBRO" — necesario para respuesta de login (HU2). */
    private final String rol;

    public CuentaUsuario(Long id, String nombre, String correo,
                         String hashContrasena, Long idHogar) {
        validarNombre(nombre);
        validarCorreo(correo);
        validarHash(hashContrasena);
        this.id             = id;
        this.nombre         = nombre.trim();
        this.correo         = correo.trim().toLowerCase();
        this.hashContrasena = hashContrasena;
        this.idHogar        = idHogar;
        // Nuevos usuarios sin hogar son MIEMBRO por defecto
        this.rol            = "MIEMBRO";
    }

    /** Constructor privado para reconstrucción desde persistencia con rol explícito. */
    private CuentaUsuario(Long id, String nombre, String correo,
                          String hashContrasena, Long idHogar, String rol) {
        this.id             = id;
        this.nombre         = nombre;
        this.correo         = correo;
        this.hashContrasena = hashContrasena;
        this.idHogar        = idHogar;
        this.rol            = rol != null ? rol : "MIEMBRO";
    }

    public static CuentaUsuario reconstruir(Long id, String nombre,
                                            String correo, String hashContrasena,
                                            Long idHogar) {
        return reconstruir(id, nombre, correo, hashContrasena, idHogar, "MIEMBRO");
    }

    public static CuentaUsuario reconstruir(Long id, String nombre,
                                            String correo, String hashContrasena,
                                            Long idHogar, String rol) {
        if (id == null)             throw new IllegalArgumentException("El id es obligatorio.");
        if (nombre == null)         throw new IllegalArgumentException("El nombre es obligatorio.");
        if (correo == null)         throw new IllegalArgumentException("El correo es obligatorio.");
        if (hashContrasena == null) throw new IllegalArgumentException("El hash es obligatorio.");
        return new CuentaUsuario(id, nombre, correo, hashContrasena, idHogar, rol);
    }

    private void validarNombre(String n) {
        if (n == null || n.isBlank() || n.trim().length() < MIN_NOMBRE)
            throw new IllegalArgumentException("El nombre es obligatorio (mín. 3 caracteres).");
        if (n.trim().length() > 50)
            throw new IllegalArgumentException("El nombre no puede superar los 50 caracteres.");
    }

    private void validarCorreo(String c) {
        if (c == null || c.isBlank() || c.trim().contains(" ")
                || c.trim().length() > MAX_EMAIL
                || c.trim().startsWith(".") || c.trim().endsWith(".")
                || !EMAIL_PATTERN.matcher(c.trim()).matches())
            throw new IllegalArgumentException("Formato de correo inválido.");
    }

    private void validarHash(String h) {
        if (h == null || h.isBlank())
            throw new IllegalArgumentException("La contraseña es obligatoria.");
    }

    public Long    getId()             { return id; }
    public String  getNombre()         { return nombre; }
    public String  getCorreo()         { return correo; }
    public String  getHashContrasena() { return hashContrasena; }
    public Long    getIdHogar()        { return idHogar; }
    public boolean tieneHogar()        { return idHogar != null; }
    public String  getRol()            { return rol; }
}
