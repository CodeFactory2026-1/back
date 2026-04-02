package com.tareasdomesticas.hogar_service.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Hogar {
    private Integer idHogar;
    private String nombreHogar;
    private String descripcionHogar;
    private Usuario administrador;
    private List<Usuario> usuarios;

    public Hogar(Integer idHogar, String nombreHogar, String descripcionHogar, Usuario creador) {
        validarNombre(nombreHogar);

        this.idHogar = idHogar;
        this.nombreHogar = nombreHogar;
        this.descripcionHogar = descripcionHogar;
        this.usuarios = new ArrayList<>();

        creador.convertirEnAdministrador();
        this.administrador = creador;
        this.usuarios.add(creador);
    } 
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del hogar es obligatorio");
        }

        if (nombre.length() < 3 || nombre.length() > 50) {
            throw new IllegalArgumentException("El nombre del hogar no cumple la longitud permitida");
        }
    }
    
    public Integer getIdHogar() {
        return idHogar;
    }
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public String getNombreHogar() {
        return nombreHogar;
    }   
    public Usuario getAdministrador() {
        return administrador;
    }

}
