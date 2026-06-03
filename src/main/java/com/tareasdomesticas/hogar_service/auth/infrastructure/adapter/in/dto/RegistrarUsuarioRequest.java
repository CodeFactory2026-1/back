package com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegistrarUsuarioRequest {
    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;
    @NotBlank(message = "El correo es obligatorio.")
    private String correo;
    @NotBlank(message = "La contraseña es obligatoria.")
    private String contrasena;
    @NotBlank(message = "La confirmación de contraseña es obligatoria.")
    private String confirmacionContrasena;
}
