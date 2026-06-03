package com.tareasdomesticas.hogar_service.common.application.port.out;

/**
 * Puerto de salida compartido: resuelve el nombre de un usuario dado su id.
 * Usado por los servicios de tareas y miembros para poblar nombreUsuarioActor
 * en la tabla actividades (historial).
 */
public interface ResolverNombreUsuarioPort {
    /** @return nombre del usuario, o "Usuario desconocido" si no existe. */
    String resolverNombre(Long idUsuario);
}
