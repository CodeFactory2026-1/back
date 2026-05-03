package com.tareasdomesticas.hogar_service.tareas.domain.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.tareasdomesticas.hogar_service.tareas.domain.model.DificultadTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.PrioridadTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.Tarea;

public interface TareaRepository {
    Tarea guardar(Tarea tarea);
    Tarea actualizar(Tarea tarea);
    void eliminar(Long idTarea);

    Optional<Tarea> buscarPorId(Long idTarea);
    List<Tarea> listar();
    List<Tarea> listarPorHogar(Long hogarId);
    List<Tarea> listarPendientesPorHogar(Long hogarId, List<Long> idsYaAsignadas);

    List<Tarea> filtrar(Long hogarId, String nombre, DificultadTarea dificultad, PrioridadTarea prioridad);

    boolean existeTareaConMismoNombreEnSemana(String nombre, LocalDateTime fechaLimite, Long hogarId);
}
