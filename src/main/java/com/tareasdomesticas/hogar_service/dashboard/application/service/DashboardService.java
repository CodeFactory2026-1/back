package com.tareasdomesticas.hogar_service.dashboard.application.service;

import com.tareasdomesticas.hogar_service.dashboard.application.dto.DashboardDTO;
import com.tareasdomesticas.hogar_service.dashboard.application.dto.DashboardDTO.CargaMiembroDTO;
import com.tareasdomesticas.hogar_service.dashboard.application.dto.DashboardDTO.ProximoVencimientoDTO;
import com.tareasdomesticas.hogar_service.dashboard.application.port.in.ObtenerDashboardUseCase;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.tareas.domain.model.AsignacionSemanalTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.EstadoTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.Tarea;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.AsignacionSemanalRepository;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardService implements ObtenerDashboardUseCase {

    private final TareaRepository             tareaRepo;
    private final AsignacionSemanalRepository asignacionRepo;
    private final HogarRepository             hogarRepo;

    public DashboardService(TareaRepository tareaRepo,
                            AsignacionSemanalRepository asignacionRepo,
                            HogarRepository hogarRepo) {
        this.tareaRepo      = tareaRepo;
        this.asignacionRepo = asignacionRepo;
        this.hogarRepo      = hogarRepo;
    }

    @Override
    public DashboardDTO obtener(Long idHogar, Long idUsuario, boolean esAdministrador) {
        if (idHogar == null)   throw new IllegalArgumentException("El idHogar es obligatorio.");
        if (idUsuario == null) throw new IllegalArgumentException("El idUsuario es obligatorio.");

        Hogar hogar = hogarRepo.buscarPorId(idHogar)
                .orElseThrow(() -> new IllegalArgumentException("El hogar no existe."));

        List<Tarea> todasLasTareas                       = tareaRepo.listarPorHogar(idHogar);
        List<AsignacionSemanalTarea> todasLasAsignaciones =
                asignacionRepo.listarAsignacionesActivasPorHogar(idHogar);

        // ── Filtro por rol ─────────────────────────────────────────────────────
        List<Tarea> tareas;
        List<AsignacionSemanalTarea> asignaciones;

        if (esAdministrador) {
            tareas       = todasLasTareas;
            asignaciones = todasLasAsignaciones;
        } else {
            Set<Long> idsTareasDelUsuario = todasLasAsignaciones.stream()
                    .filter(a -> idUsuario.equals(a.getIdUsuarioAsignado()))
                    .map(AsignacionSemanalTarea::getIdTarea)
                    .collect(Collectors.toSet());
            tareas       = todasLasTareas.stream()
                    .filter(t -> idsTareasDelUsuario.contains(t.getIdTarea()))
                    .toList();
            asignaciones = todasLasAsignaciones.stream()
                    .filter(a -> idUsuario.equals(a.getIdUsuarioAsignado()))
                    .toList();
        }

        // ── Conteos por estado ─────────────────────────────────────────────────
        // "Pospuesta" = excedente=true (sobró en la distribución semanal).
        // "Pendiente" = tiene asignación activa con estado PENDIENTE y excedente=false.
        // Las tareas sin ninguna asignación activa también son Pendientes.

        long pospuestas = asignaciones.stream()
                .filter(AsignacionSemanalTarea::isExcedente)
                .count();

        Map<EstadoTarea, Long> countByEstado = asignaciones.stream()
                .filter(a -> !a.isExcedente())   // excedentes ya contados arriba
                .collect(Collectors.groupingBy(AsignacionSemanalTarea::getEstado,
                        Collectors.counting()));

        long finalizadas = countByEstado.getOrDefault(EstadoTarea.FINALIZADO, 0L);
        long asignadas   = countByEstado.getOrDefault(EstadoTarea.ASIGNADO,   0L);
        long enProceso   = countByEstado.getOrDefault(EstadoTarea.EN_PROCESO,  0L);

        // Tareas que no tienen asignación activa de ningún tipo = pendientes puras
        Set<Long> idsTareasConAsignacion = asignaciones.stream()
                .map(AsignacionSemanalTarea::getIdTarea)
                .collect(Collectors.toSet());
        long pendientes = tareas.stream()
                .filter(t -> !idsTareasConAsignacion.contains(t.getIdTarea()))
                .count()
                // + las que tienen asignación activa con estado PENDIENTE y excedente=false
                + countByEstado.getOrDefault(EstadoTarea.PENDIENTE, 0L);

        long totalTareas = tareas.size();
        double porcentaje = totalTareas == 0 ? 0.0
                : Math.round((finalizadas * 100.0 / totalTareas) * 10.0) / 10.0;

        // ── Carga por miembro ──────────────────────────────────────────────────
        Map<Long, Long> cargaMap = todasLasAsignaciones.stream()
                .filter(a -> a.getIdUsuarioAsignado() != null && !a.isExcedente())
                .collect(Collectors.groupingBy(AsignacionSemanalTarea::getIdUsuarioAsignado,
                        Collectors.counting()));

        List<CargaMiembroDTO> cargaPorMiembro;
        if (esAdministrador) {
            cargaPorMiembro = hogar.getUsuarios().stream()
                    .map(u -> new CargaMiembroDTO(
                            u.getIdUsuario(),
                            u.getNombreUsuario(),
                            cargaMap.getOrDefault(u.getIdUsuario(), 0L)))
                    .toList();
        } else {
            String nombreUsuario = hogar.getUsuarios().stream()
                    .filter(u -> idUsuario.equals(u.getIdUsuario()))
                    .map(u -> u.getNombreUsuario())
                    .findFirst().orElse("Usuario");
            cargaPorMiembro = List.of(
                    new CargaMiembroDTO(idUsuario, nombreUsuario,
                            cargaMap.getOrDefault(idUsuario, 0L)));
        }

        // ── Próximos vencimientos ──────────────────────────────────────────────
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime hasta = ahora.plusDays(7);

        List<ProximoVencimientoDTO> proximos = tareas.stream()
                .filter(t -> t.getFechaLimite() != null
                        && t.getFechaLimite().isBefore(hasta))
                .sorted(Comparator.comparing(Tarea::getFechaLimite))
                .map(t -> {
                    LocalDateTime fl = t.getFechaLimite();
                    long dias = ChronoUnit.DAYS.between(ahora.toLocalDate(), fl.toLocalDate());
                    boolean vencida   = fl.isBefore(ahora);
                    boolean resaltada = vencida || dias <= 1;
                    return new ProximoVencimientoDTO(
                            t.getIdTarea(), t.getNombreTarea(), fl, dias, vencida, resaltada);
                })
                .toList();

        return new DashboardDTO(totalTareas, pendientes, pospuestas, asignadas,
                enProceso, finalizadas, porcentaje, cargaPorMiembro, proximos);
    }
}
