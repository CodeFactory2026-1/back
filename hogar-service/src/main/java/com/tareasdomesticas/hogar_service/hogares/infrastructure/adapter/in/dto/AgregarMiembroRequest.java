package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AgregarMiembroRequest {
    @NotNull(message = "El id del administrador es obligatorio")
    private Long idAdministrador;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    public Long getIdAdministrador()       { return idAdministrador; }
    public void setIdAdministrador(Long v) { this.idAdministrador = v; }
    public String getNombre()                 { return nombre; }
    public void setNombre(String v)           { this.nombre = v; }
    public String getCorreo()                 { return correo; }
    public void setCorreo(String v)           { this.correo = v; }
}
