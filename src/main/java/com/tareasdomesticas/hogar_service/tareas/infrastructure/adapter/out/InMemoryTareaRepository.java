package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

import com.tareasdomesticas.hogar_service.tareas.domain.model.Tarea;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

public class InMemoryTareaRepository implements TareaRepository {
    private final List<Tarea> tareas = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public synchronized Tarea guardar(Tarea tarea) {
        if (existeTareaConMismoNombreEnSemana(tarea.getNombreTarea(), tarea.getFechaLimite(), tarea.getIdHogar())) {
            throw new IllegalArgumentException(
                    "Ya existe una tarea con el mismo nombre en esta semana para este hogar");
        }
        Tarea tareaToSave = tarea;
        if (tarea.getIdTarea() == null) {
            tareaToSave = new Tarea(
                    idGenerator.getAndIncrement(),
                    tarea.getIdHogar(),
                    tarea.getNombreTarea(),
                    tarea.getFotoTarea(),
                    tarea.getFechaLimite(),
                    tarea.getDificultad(),
                    tarea.getPrioridad());
        }

        tareas.add(tareaToSave);
        return tareaToSave;
    }

    @Override
    public synchronized List<Tarea> listar() {
        return Collections.unmodifiableList(new ArrayList<>(tareas));
    }

    public synchronized List<Tarea> listarPorHogar(Long hogarId) {
        return tareas.stream()
                .filter(t -> t.getIdHogar().equals(hogarId))
                .collect(Collectors.toList());
    }

    public synchronized List<Tarea> listarPendientesPorHogar(Long hogarId) {
    return tareas.stream()
                 .filter(t -> t.getIdHogar().equals(hogarId) && t.esPendiente())
                 .collect(Collectors.toList());
    }
    public synchronized boolean existeTareaConMismoNombreEnSemana(String nombre, LocalDateTime fechaLimite, Long hogarId) {
        int semana = fechaLimite.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return tareas.stream()
                .anyMatch(t -> t.getNombreTarea().equalsIgnoreCase(nombre)
                            && t.getSemana() == semana
                            && t.getIdHogar().equals(hogarId));
    }

}