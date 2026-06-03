package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.common.application.port.out.ResolverNombreUsuarioPort;
import com.tareasdomesticas.hogar_service.historial.application.port.in.RegistrarAccionHistorialUseCase;
import com.tareasdomesticas.hogar_service.historial.domain.model.TipoAccion;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.EliminarTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.EstadoTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.Tarea;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.AsignacionSemanalRepository;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

public class EliminarTareaService implements EliminarTareaUseCase {

    private final TareaRepository tareaRepository;
    private final AsignacionSemanalRepository asignacionRepository;
    private final RegistrarAccionHistorialUseCase historial;
    private final ResolverNombreUsuarioPort resolverNombre;

    public EliminarTareaService(TareaRepository tareaRepository,
            AsignacionSemanalRepository asignacionRepository,
            RegistrarAccionHistorialUseCase historial,
            ResolverNombreUsuarioPort resolverNombre) {
        this.tareaRepository = tareaRepository;
        this.asignacionRepository = asignacionRepository;
        this.historial = historial;
        this.resolverNombre = resolverNombre;
    }

    @Override
    public void eliminarTarea(Long idTarea, Long idUsuarioAdmin) {
        try {
            Tarea tarea = tareaRepository.buscarPorId(idTarea)
                    .orElseThrow(() -> new IllegalArgumentException("La tarea no existe."));

            asignacionRepository
                    .buscarAsignacionActivaDeTarea(tarea.getIdTarea(), tarea.getIdHogar())
                    .ifPresent(ast -> {
                        if (ast.getEstado() == EstadoTarea.ASIGNADO
                                || ast.getEstado() == EstadoTarea.EN_PROCESO)
                            throw new IllegalStateException(
                                    "No se puede eliminar una tarea asignada o en progreso.");
                    });

            Long idHogar = tarea.getIdHogar();
            String nombreTarea = tarea.getNombreTarea();

            tareaRepository.eliminar(idTarea);

            // Actor = administrador que ejecutó la eliminación (viene del request)
            String nombreAdmin = resolverNombre.resolverNombre(idUsuarioAdmin);
            String detalle = "Tarea eliminada: " + nombreTarea + " por " + nombreAdmin;
            historial.registrar(idHogar, null, nombreTarea,
                    TipoAccion.TAREA_ELIMINADA, idUsuarioAdmin, nombreAdmin, detalle);

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Algo salió mal, inténtelo de nuevo.", e);
        }
    }
}
