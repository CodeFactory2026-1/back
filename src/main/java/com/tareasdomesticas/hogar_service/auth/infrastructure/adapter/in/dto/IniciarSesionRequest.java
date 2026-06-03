package com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IniciarSesionRequest {
    @NotBlank(message = "El correo es obligatorio.")
    private String correo;
    @NotBlank(message = "La contraseña es obligatoria.")
    private String contrasena;
}
