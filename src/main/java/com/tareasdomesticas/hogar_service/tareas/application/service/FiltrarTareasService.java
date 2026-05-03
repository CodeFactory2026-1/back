package com.tareasdomesticas.hogar_service.tareas.application.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tareasdomesticas.hogar_service.tareas.application.assembler.TareaListadoAssembler;
import com.tareasdomesticas.hogar_service.tareas.application.dto.*;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.FiltrarTareasUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.*;

public class FiltrarTareasService implements FiltrarTareasUseCase {

    private final TareaRepository tareaRepository;
    private final AsignacionSemanalRepository asignacionRepository;

    public FiltrarTareasService(TareaRepository tareaRepository,
                                AsignacionSemanalRepository asignacionRepository) {
        this.tareaRepository      = tareaRepository;
        this.asignacionRepository = asignacionRepository;
    }

    @Override
    public List<TareaListadoDTO> filtrar(FiltroTareasDTO filtro) {
        DificultadTarea dificultad = parseEnum(DificultadTarea.class, filtro.dificultad());
        PrioridadTarea  prioridad  = parseEnum(PrioridadTarea.class,  filtro.prioridad());
        EstadoTarea     estado     = parseEnum(EstadoTarea.class,     filtro.estado());

        List<Tarea> tareas = tareaRepository.filtrar(
                filtro.hogarId(), filtro.nombre(), dificultad, prioridad);

        return tareas.stream()
                .map(t -> {
                    AsignacionSemanalTarea ast = asignacionRepository
                            .buscarAsignacionActivaDeTarea(t.getIdTarea(), t.getIdHogar())
                            .orElse(null);
                    return new TareaConAst(t, ast);
                })
                .filter(ta -> estado == null || estadoEfectivo(ta.ast()).equals(estado))
                .filter(ta -> filtro.idUsuario() == null
                        || usuarioCoincide(ta.ast(), filtro.idUsuario()))
                .map(ta -> TareaListadoAssembler.toDTO(ta.tarea(), ta.ast()))
                .collect(Collectors.toList());
    }

    private EstadoTarea estadoEfectivo(AsignacionSemanalTarea ast) {
        return ast != null ? ast.getEstado() : EstadoTarea.PENDIENTE;
    }

    private boolean usuarioCoincide(AsignacionSemanalTarea ast, Long idUsuario) {
        return ast != null && idUsuario.equals(ast.getIdUsuarioAsignado());
    }

    private <T extends Enum<T>> T parseEnum(Class<T> cls, String value) {
        if (value == null || value.isBlank()) return null;
        try { return Enum.valueOf(cls, value.toUpperCase()); }
        catch (Exception e) { throw new IllegalArgumentException("Valor no válido: " + value); }
    }

    private record TareaConAst(Tarea tarea, AsignacionSemanalTarea ast) {}
}
