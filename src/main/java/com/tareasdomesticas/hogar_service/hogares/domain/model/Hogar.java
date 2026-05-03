package com.tareasdomesticas.hogar_service.hogares.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;

public class Hogar {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?!\\.)(?!.*\\.\\.)[a-zA-Z0-9_+\\-.]+@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}$");

    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MAX_DESCRIPCION_LEN = 255;

    private Long idHogar;
    private String nombreHogar;
    private String descripcionHogar;
    private List<Usuario> usuarios;

    public Hogar(Long idHogar, String nombreHogar, String descripcionHogar, Usuario creador) {
        validarNombre(nombreHogar);
        validarDescripcionHogar(descripcionHogar);

        if (creador == null) {
            throw new IllegalArgumentException("El creador es obligatorio.");
        }

        this.idHogar = idHogar;
        this.nombreHogar = nombreHogar;
        this.descripcionHogar = descripcionHogar;
        this.usuarios = new ArrayList<>();
        creador.convertirEnAdministrador();

        if (idHogar != null) {
            creador.asignarHogar(idHogar);
        }

        this.usuarios.add(creador);
    }

    public void cargarMiembro(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public Usuario getAdministrador() {
        return usuarios.stream()
                .filter(Usuario::esAdministrador)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("El hogar no tiene administrador."));
    }

 
    public void agregarMiembro(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }

        validarNombreMiembro(usuario.getNombreUsuario());
        validarCorreo(usuario.getCorreoUsuario());

        boolean duplicado = usuarios.stream()
                .anyMatch(u -> u.getCorreoUsuario()
                        .equalsIgnoreCase(usuario.getCorreoUsuario()));

        if (duplicado) {
            throw new IllegalStateException("El correo ya está en uso en este hogar.");
        }

        if (this.idHogar != null) {
            usuario.asignarHogar(this.idHogar);
        }

        this.usuarios.add(usuario);
    }

    public void eliminarMiembro(Long idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El id del usuario es obligatorio.");
        }

        Usuario admin = getAdministrador();

        if (admin.getIdUsuario().equals(idUsuario)) {
            throw new IllegalStateException("No se puede eliminar al administrador del hogar.");
        }

        boolean removido = usuarios.removeIf(u -> idUsuario.equals(u.getIdUsuario()));

        if (!removido) {
            throw new IllegalStateException("El usuario no pertenece a este hogar.");
        }
    }

 
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del hogar es obligatorio.");
        } else if (nombre.trim().length() < 3 || nombre.trim().length() > 50) {
            throw new IllegalArgumentException("El nombre del hogar no cumple la longitud permitida.");
        }
    }

    private void validarDescripcionHogar(String descripcion) {
        if (descripcion != null && descripcion.length() > MAX_DESCRIPCION_LEN) {
            throw new IllegalArgumentException(
                    "La descripción del hogar no puede superar los " + MAX_DESCRIPCION_LEN + " caracteres.");
        }
    }

    private void validarNombreMiembro(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del miembro es obligatorio.");
        }
    }

    private void validarCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        correo = correo.trim();
        
        if (correo.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException("El correo supera la longitud máxima permitida.");
        }
        if (correo.startsWith(".") || correo.endsWith(".")) {
            throw new IllegalArgumentException("El correo no puede empezar ni terminar con punto.");
        }
        if (!EMAIL_PATTERN.matcher(correo).matches()) {
            throw new IllegalArgumentException("El formato del correo no es válido.");
        }
        if (correo.contains("@")) {
            String parteLocal = correo.substring(0, correo.indexOf('@'));
            if (parteLocal.endsWith(".")) {
                throw new IllegalArgumentException("El formato del correo no es válido.");
            }
        }
    }
    public Long getIdHogar() {
        return idHogar;
    }

    public String getNombreHogar() {
        return nombreHogar;
    }

    public String getDescripcionHogar() {
        return descripcionHogar;
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Long> getIdsUsuarios() {
        return usuarios.stream()
                .filter(u -> u.getIdUsuario() != null)
                .map(Usuario::getIdUsuario)
                .toList();
    }
}