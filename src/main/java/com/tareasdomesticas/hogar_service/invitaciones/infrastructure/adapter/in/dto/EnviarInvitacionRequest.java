package com.tareasdomesticas.hogar_service.invitaciones.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnviarInvitacionRequest {
    private Long idAdministrador;

    // HU6: "El nombre es obligatorio (mín. 2 caracteres)." — se valida en el
    // dominio Invitacion
    @NotBlank(message = "El nombre es obligatorio (mín. 2 caracteres).")
    private String nombre;

    // HU6: "El correo es obligatorio." — formato se valida en dominio con mensaje
    // "Formato de correo inválido."
    @NotBlank(message = "El correo es obligatorio.")
    private String correo;
}
