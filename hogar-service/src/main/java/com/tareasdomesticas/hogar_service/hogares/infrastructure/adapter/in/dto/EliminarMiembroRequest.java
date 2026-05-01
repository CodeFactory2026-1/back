package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotNull;

public class EliminarMiembroRequest {
    @NotNull(message = "El id del administrador es obligatorio")
    private Long idAdministrador;
    @NotNull(message = "El id del miembro es obligatorio")
    private Long idMiembro;

    public Long getIdAdministrador()       { return idAdministrador; }
    public void setIdAdministrador(Long v) { this.idAdministrador = v; }
    public Long getIdMiembro()             { return idMiembro; }
    public void setIdMiembro(Long v)       { this.idMiembro = v; }
}
