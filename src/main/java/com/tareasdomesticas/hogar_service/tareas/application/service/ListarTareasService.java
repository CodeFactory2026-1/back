package com.tareasdomesticas.hogar_service.tareas.application.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tareasdomesticas.hogar_service.tareas.application.assembler.TareaListadoAssembler;
import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.ListarTareasUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.*;

public class ListarTareasService implements ListarTareasUseCase {

    private final TareaRepository tareaRepository;
    private final AsignacionSemanalRepository asignacionRepository;

    public ListarTareasService(TareaRepository tareaRepository,
                               AsignacionSemanalRepository asignacionRepository) {
        this.tareaRepository      = tareaRepository;
        this.asignacionRepository = asignacionRepository;
    }

    @Override
    public List<TareaListadoDTO> listarTodas() {
        return tareaRepository.listar().stream()
                .map(t -> TareaListadoAssembler.toDTO(t, buscarAst(t)))
                .collect(Collectors.toList());
    }

    @Override
    public List<TareaListadoDTO> listarPorHogar(Long hogarId) {
        return tareaRepository.listarPorHogar(hogarId).stream()
                .map(t -> TareaListadoAssembler.toDTO(t, buscarAst(t)))
                .collect(Collectors.toList());
    }

    private AsignacionSemanalTarea buscarAst(Tarea t) {
        return asignacionRepository
                .buscarAsignacionActivaDeTarea(t.getIdTarea(), t.getIdHogar())
                .orElse(null);
    }
}
