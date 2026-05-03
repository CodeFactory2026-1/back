package com.tareasdomesticas.hogar_service.tareas.domain.model;

import java.time.LocalDate;
import java.time.temporal.IsoFields;

public class AsignacionSemanal {

    private Long idAsignacion;
    private Long idHogar;
    private LocalDate fechaAsignacion;

    public AsignacionSemanal(Long idAsignacion, Long idHogar, LocalDate fechaAsignacion) {
        if (idHogar == null)
            throw new IllegalArgumentException("El idHogar es obligatorio.");
        if (fechaAsignacion == null)
            throw new IllegalArgumentException("La fecha de asignación es obligatoria.");
        this.idAsignacion    = idAsignacion;
        this.idHogar         = idHogar;
        this.fechaAsignacion = fechaAsignacion;
    }

    public Long getIdAsignacion()         { return idAsignacion; }
    public Long getIdHogar()              { return idHogar; }
    public LocalDate getFechaAsignacion() { return fechaAsignacion; }

    /**
     * Verifica si esta asignación pertenece a la semana ISO actual.
     * Usa semana ISO (lunes–domingo) en lugar de un rango de 7 días fijos,
     * evitando que una asignación del domingo de la semana pasada siga
     * considerándose activa el lunes siguiente.
     */
    public boolean perteneceASemanaActual() {
        LocalDate hoy = LocalDate.now();
        return fechaAsignacion.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                    == hoy.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
               && fechaAsignacion.getYear() == hoy.getYear();
    }
}
