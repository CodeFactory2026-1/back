package com.tareasdomesticas.hogar_service.tareas.application.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.AsignarTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.DificultadTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.Tarea;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

public class AsignarTareaService implements AsignarTareaUseCase {

    private final TareaRepository tareaRepository;
    private final HogarRepository hogarRepository;
    private LocalDate ultimaAsignacion = null;

    public AsignarTareaService(TareaRepository tareaRepository, HogarRepository hogarRepository) {
        this.tareaRepository = tareaRepository;
        this.hogarRepository = hogarRepository;
    }

    @Override
    public Map<String, Object> asignarTareasSemanales(Long hogarId) {

        if (ultimaAsignacion != null &&
            ultimaAsignacion.plusDays(7).isAfter(LocalDate.now())) {
            throw new IllegalStateException("Solo se puede asignar una vez por semana");
        }

        Hogar hogar = hogarRepository.buscarPorId(hogarId)
                .orElseThrow(() -> new IllegalStateException("No se encontró el hogar con id: " + hogarId));

        List<Long> usuarios = hogar.getUsuarios().stream()
                .map(u -> u.getIdUsuario().longValue())
                .collect(Collectors.toList());

        if (usuarios.isEmpty()) {
            throw new IllegalStateException("No hay usuarios en el hogar");
        }

        List<Tarea> tareas = tareaRepository.listarPendientesPorHogar(hogarId);
        if (tareas.isEmpty()) {
            throw new IllegalStateException("No hay tareas para asignar");
        }

        tareas.sort(Comparator
                .<Tarea, Boolean>comparing(Tarea::isExcedente).reversed()
                .thenComparing(Tarea::getDificultad, Comparator.reverseOrder()));

        int totalAAsignar;
        if (tareas.size() <= usuarios.size()) {
            totalAAsignar = tareas.size();
        } else {
            int tareasPorUsuario = tareas.size() / usuarios.size();
            totalAAsignar = tareasPorUsuario * usuarios.size();
        }

        List<Tarea> tareasAAsignar  = new ArrayList<>(tareas.subList(0, totalAAsignar));
        List<Tarea> tareasExcedentes = new ArrayList<>(tareas.subList(totalAAsignar, tareas.size()));

        Map<Long, Integer> cargaPorUsuario = new HashMap<>();
        for (Long u : usuarios) {
            cargaPorUsuario.put(u, 0);
        }

        List<Map<String, Object>> asignadas = new ArrayList<>();
        for (Tarea tarea : tareasAAsignar) {
            Long usuarioId = obtenerUsuarioMenorCarga(cargaPorUsuario);
            tarea.asignarA(usuarioId);
            int nuevaCarga = cargaPorUsuario.get(usuarioId) + peso(tarea.getDificultad());
            cargaPorUsuario.put(usuarioId, nuevaCarga);

            Map<String, Object> detalle = new LinkedHashMap<>();
            detalle.put("idTarea", tarea.getIdTarea());
            detalle.put("nombre", tarea.getNombreTarea());
            detalle.put("dificultad", tarea.getDificultad());
            detalle.put("usuarioAsignado", usuarioId);
            detalle.put("estado", tarea.getEstado());
            asignadas.add(detalle);
        }

        List<Map<String, Object>> excedentes = new ArrayList<>();
        for (Tarea tarea : tareasExcedentes) {
            tarea.marcarComoExcedente();   
            Map<String, Object> detalle = new LinkedHashMap<>();
            detalle.put("idTarea", tarea.getIdTarea());
            detalle.put("nombre", tarea.getNombreTarea());
            detalle.put("dificultad", tarea.getDificultad());
            detalle.put("estado", tarea.getEstado());
            excedentes.add(detalle);
        }

        ultimaAsignacion = LocalDate.now();

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("mensaje", "Tareas asignadas correctamente");
        resultado.put("hogarId", hogarId);
        resultado.put("tareasAsignadas", asignadas);
        resultado.put("tareasExcedentes", excedentes);
        return resultado;
    }

    private int peso(DificultadTarea d) {
        return switch (d) {
            case ALTA -> 3;
            case MEDIA -> 2;
            case BAJA -> 1;
        };
    }

    private Long obtenerUsuarioMenorCarga(Map<Long, Integer> cargaPorUsuario) {
        int minCarga = Collections.min(cargaPorUsuario.values());
        List<Long> candidatos = cargaPorUsuario.entrySet().stream()
                .filter(e -> e.getValue() == minCarga)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        Collections.shuffle(candidatos);
        return candidatos.get(0);
    }
}