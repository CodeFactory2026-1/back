package com.tareasdomesticas.hogar_service.tareas.application.dto;

import java.util.List;

public class AsignacionSemanalResponse {

    private String mensaje;
    private Long idHogar;
    private List<TareaAsignadaDTO> tareasAsignadas;
    private List<TareaExcedenteDTO> tareasExcedentes;

    public AsignacionSemanalResponse(String mensaje, Long idHogar,
            List<TareaAsignadaDTO> tareasAsignadas,
            List<TareaExcedenteDTO> tareasExcedentes) {
        this.mensaje          = mensaje;
        this.idHogar          = idHogar;
        this.tareasAsignadas  = tareasAsignadas;
        this.tareasExcedentes = tareasExcedentes;
    }

    public String getMensaje()                              { return mensaje; }
    public Long getIdHogar()                               { return idHogar; }
    public List<TareaAsignadaDTO> getTareasAsignadas()     { return tareasAsignadas; }
    public List<TareaExcedenteDTO> getTareasExcedentes()   { return tareasExcedentes; }
}
