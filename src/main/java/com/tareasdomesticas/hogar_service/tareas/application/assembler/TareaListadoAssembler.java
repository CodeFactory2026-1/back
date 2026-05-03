package com.tareasdomesticas.hogar_service.tareas.application.assembler;

import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;
import com.tareasdomesticas.hogar_service.tareas.application.dto.UsuarioAsignadoDTO;
import com.tareasdomesticas.hogar_service.tareas.domain.model.AsignacionSemanalTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.EstadoTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.Tarea;

/**
 * Ensamblador centralizado para convertir Tarea + AsignacionSemanalTarea → TareaListadoDTO.
 * Principio SRP: única clase responsable de esta conversión.
 * Principio DRY: evita duplicar toDTO() en los cuatro servicios que lo necesitaban.
 */
public final class TareaListadoAssembler {

    private TareaListadoAssembler() {}

    public static TareaListadoDTO toDTO(Tarea t, AsignacionSemanalTarea ast) {
        String estado = ast != null
                ? ast.getEstado().name()
                : EstadoTarea.PENDIENTE.name();

        UsuarioAsignadoDTO usuario = (ast != null && ast.getIdUsuarioAsignado() != null)
                ? UsuarioAsignadoDTO.asignado(ast.getIdUsuarioAsignado())
                : UsuarioAsignadoDTO.sinAsignar();

        return new TareaListadoDTO(
                t.getIdTarea(),
                t.getIdHogar(),
                t.getNombreTarea(),
                t.getDescripcionTarea(),
                t.getDificultad() != null ? t.getDificultad().name() : null,
                t.getPrioridad()  != null ? t.getPrioridad().name()  : null,
                estado,
                usuario,
                t.getFechaLimite());
    }
}
