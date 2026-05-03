package com.tareasdomesticas.hogar_service.tareas.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.tareasdomesticas.hogar_service.tareas.domain.model.AsignacionSemanal;
import com.tareasdomesticas.hogar_service.tareas.domain.model.AsignacionSemanalTarea;

public interface AsignacionSemanalRepository {

    AsignacionSemanal guardarAsignacion(AsignacionSemanal asignacion);
    Optional<AsignacionSemanal> obtenerUltimaAsignacion(Long hogarId);

    AsignacionSemanalTarea guardarAsignacionTarea(AsignacionSemanalTarea ast);
    AsignacionSemanalTarea actualizarAsignacionTarea(AsignacionSemanalTarea ast);

    Optional<AsignacionSemanalTarea> buscarAsignacionTarea(Long idAsignacion, Long idTarea);
    List<AsignacionSemanalTarea> listarPorAsignacion(Long idAsignacion);
    List<AsignacionSemanalTarea> listarPorUsuario(Long idUsuario, Long idAsignacion);
    Optional<AsignacionSemanalTarea> buscarAsignacionActivaDeTarea(Long idTarea, Long hogarId);

    void liberarTareasDeUsuario(Long idUsuario, Long hogarId);
    List<Long> obtenerIdsTareasExcedentes(Long hogarId);
}
