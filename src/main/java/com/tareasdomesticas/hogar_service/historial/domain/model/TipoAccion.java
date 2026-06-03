package com.tareasdomesticas.hogar_service.historial.domain.model;

/**
 * Tipos de acción auditables.
 * Los nombres coinciden exactamente con los valores del tipo
 * Postgres 'tipo_accion_enum' definido en el DDL:
 * TAREA_CREADA, TAREA_EDITADA, TAREA_ELIMINADA, TAREA_ASIGNADA,
 * TAREA_ESTADO_CAMBIADO, MIEMBRO_AGREGADO, MIEMBRO_ELIMINADO,
 * INVITACION_ENVIADA, INVITACION_ACEPTADA, INVITACION_RECHAZADA
 */
public enum TipoAccion {
    TAREA_CREADA,
    TAREA_EDITADA,
    TAREA_ELIMINADA,
    TAREA_POSPUESTA,
    TAREA_ASIGNADA,
    TAREA_ESTADO_CAMBIADO,
    MIEMBRO_AGREGADO,
    MIEMBRO_ELIMINADO,
    INVITACION_ENVIADA,
    INVITACION_ACEPTADA,
    INVITACION_RECHAZADA
}
