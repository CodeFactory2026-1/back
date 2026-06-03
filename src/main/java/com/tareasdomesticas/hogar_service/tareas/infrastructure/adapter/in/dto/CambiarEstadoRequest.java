package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarEstadoRequest {

    @NotBlank(message = "El nuevo estado es obligatorio.")
    private String nuevoEstado;

    private Long idUsuarioActor;
}
