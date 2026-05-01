package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.tareas.application.port.in.EliminarTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.EstadoTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.Tarea;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.AsignacionSemanalRepository;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

public class EliminarTareaService implements EliminarTareaUseCase {

    private final TareaRepository tareaRepository;
    private final AsignacionSemanalRepository asignacionRepository;

    public EliminarTareaService(TareaRepository tareaRepository,
                                AsignacionSemanalRepository asignacionRepository) {
        this.tareaRepository      = tareaRepository;
        this.asignacionRepository = asignacionRepository;
    }

    @Override
    public void eliminarTarea(Long idTarea) {
        try {
            Tarea tarea = tareaRepository.buscarPorId(idTarea)
                    .orElseThrow(() -> new IllegalArgumentException("La tarea no existe."));
            asignacionRepository.buscarAsignacionActivaDeTarea(tarea.getIdTarea(), tarea.getIdHogar())
                    .ifPresent(ast -> {
                        if (ast.getEstado() == EstadoTarea.ASIGNADO
                                || ast.getEstado() == EstadoTarea.EN_PROCESO)
                            throw new IllegalStateException(
                                    "No se puede eliminar una tarea asignada o en progreso.");
                    });

            tareaRepository.eliminar(idTarea);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Algo salió mal, inténtelo de nuevo.", e);
        }
    }
}
