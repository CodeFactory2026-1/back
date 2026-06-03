package com.tareasdomesticas.hogar_service.tareas.application.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.tareasdomesticas.hogar_service.common.application.port.out.ResolverNombreUsuarioPort;
import com.tareasdomesticas.hogar_service.historial.application.port.in.RegistrarAccionHistorialUseCase;
import com.tareasdomesticas.hogar_service.historial.domain.model.TipoAccion;
import com.tareasdomesticas.hogar_service.tareas.application.dto.*;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.AsignarTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.application.port.out.ObtenerMiembrosHogarPort;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.*;

public class AsignarTareaService implements AsignarTareaUseCase {

        private final TareaRepository tareaRepository;
        private final AsignacionSemanalRepository asignacionRepository;
        private final ObtenerMiembrosHogarPort obtenerMiembrosPort;
        private final RegistrarAccionHistorialUseCase historial;
        private final ResolverNombreUsuarioPort resolverNombre;

        public AsignarTareaService(TareaRepository tareaRepository,
                        AsignacionSemanalRepository asignacionRepository,
                        ObtenerMiembrosHogarPort obtenerMiembrosPort,
                        RegistrarAccionHistorialUseCase historial,
                        ResolverNombreUsuarioPort resolverNombre) {
                this.tareaRepository = tareaRepository;
                this.asignacionRepository = asignacionRepository;
                this.obtenerMiembrosPort = obtenerMiembrosPort;
                this.historial = historial;
                this.resolverNombre = resolverNombre;
        }

        @Override
        public AsignacionSemanalResponse asignarTareasSemanales(Long hogarId) {

                asignacionRepository.obtenerUltimaAsignacion(hogarId).ifPresent(ultima -> {
                        if (ultima.perteneceASemanaActual())
                                throw new IllegalStateException("Solo se puede asignar una vez por semana");
                });

                List<Long> usuarios = obtenerMiembrosPort.obtenerIdsUsuarios(hogarId);
                if (usuarios == null || usuarios.isEmpty())
                        throw new IllegalStateException("No se encontró el hogar: " + hogarId);

                List<Long> idsExcedentes = asignacionRepository.obtenerIdsTareasExcedentes(hogarId);
                List<Long> idsYaAsignados = asignacionRepository
                                .obtenerUltimaAsignacion(hogarId)
                                .map(a -> asignacionRepository.listarPorAsignacion(a.getIdAsignacion())
                                                .stream().map(AsignacionSemanalTarea::getIdTarea).toList())
                                .orElse(List.of());

                List<Tarea> tareas = new ArrayList<>(
                                tareaRepository.listarPendientesPorHogar(hogarId, idsYaAsignados));

                if (tareas.isEmpty())
                        throw new IllegalStateException("No hay tareas para asignar");

                Set<Long> excedentesSet = new HashSet<>(idsExcedentes);
                tareas.sort(Comparator
                                .<Tarea, Boolean>comparing(t -> excedentesSet.contains(t.getIdTarea())).reversed()
                                .thenComparing(Tarea::getDificultad, Comparator.reverseOrder()));

                int totalAAsignar = tareas.size() <= usuarios.size()
                                ? tareas.size()
                                : (tareas.size() / usuarios.size()) * usuarios.size();

                List<Tarea> aAsignar = new ArrayList<>(tareas.subList(0, totalAAsignar));
                List<Tarea> excedentes = new ArrayList<>(tareas.subList(totalAAsignar, tareas.size()));

                AsignacionSemanal nuevaAsignacion = asignacionRepository.guardarAsignacion(
                                new AsignacionSemanal(null, hogarId, LocalDate.now()));

                Map<Long, Integer> carga = new HashMap<>();
                usuarios.forEach(u -> carga.put(u, 0));

                List<TareaAsignadaDTO> asignadas = new ArrayList<>();
                for (Tarea tarea : aAsignar) {
                        Long usuarioId = usuarioMenorCarga(carga);
                        String nombreMiembro = resolverNombre.resolverNombre(usuarioId);

                        AsignacionSemanalTarea ast = new AsignacionSemanalTarea(
                                        nuevaAsignacion.getIdAsignacion(), tarea.getIdTarea(), usuarioId);
                        asignacionRepository.guardarAsignacionTarea(ast);
                        carga.put(usuarioId, carga.get(usuarioId) + tarea.getDificultad().getPeso());

                        // Actor = miembro al que se le asignó la tarea
                        String detalleAsignada = "Asignada a " + nombreMiembro;
                        historial.registrar(hogarId, tarea.getIdTarea(), tarea.getNombreTarea(),
                                        TipoAccion.TAREA_ASIGNADA, usuarioId, nombreMiembro, detalleAsignada);

                        asignadas.add(new TareaAsignadaDTO(
                                        tarea.getIdTarea(), tarea.getNombreTarea(),
                                        tarea.getDificultad().name(), usuarioId, nombreMiembro,
                                        ast.getEstado().name()));
                }

                List<TareaExcedenteDTO> excedentesDTO = new ArrayList<>();
                for (Tarea tarea : excedentes) {
                        AsignacionSemanalTarea ast = new AsignacionSemanalTarea(
                                        nuevaAsignacion.getIdAsignacion(), tarea.getIdTarea());
                        asignacionRepository.guardarAsignacionTarea(ast);
                        excedentesDTO.add(new TareaExcedenteDTO(
                                        tarea.getIdTarea(), tarea.getNombreTarea(),
                                        tarea.getDificultad().name(), ast.getEstado().name()));
                        // registrar pospuesta con detalle
                        String detallePospuesta = "Tarea pospuesta por falta de cupo en asignación";
                        historial.registrar(hogarId, tarea.getIdTarea(), tarea.getNombreTarea(),
                                        TipoAccion.TAREA_POSPUESTA, null, null, detallePospuesta);
                }

                return new AsignacionSemanalResponse(
                                "Tareas asignadas correctamente", hogarId, asignadas, excedentesDTO);
        }

        private Long usuarioMenorCarga(Map<Long, Integer> carga) {
                int min = Collections.min(carga.values());
                List<Long> candidatos = carga.entrySet().stream()
                                .filter(e -> e.getValue() == min)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList());
                Collections.shuffle(candidatos);
                return candidatos.get(0);
        }
}
