package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EliminarMiembroRequest {
    private Long idAdministrador;

    @NotNull(message = "El idMiembro es obligatorio.")
    private Long idMiembro;

    // Para construir el mensaje de la HU6: "Miembro eliminado. [nombre] fue
    // eliminado del hogar."
    private String nombreMiembro;
}
