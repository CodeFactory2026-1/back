package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out;

import com.tareasdomesticas.hogar_service.tareas.application.port.out.LiberarTareasPort;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.AsignacionSemanalRepository;

public class LiberarTareasAdapter implements LiberarTareasPort {

    private final AsignacionSemanalRepository asignacionRepository;

    public LiberarTareasAdapter(AsignacionSemanalRepository asignacionRepository) {
        this.asignacionRepository = asignacionRepository;
    }

    @Override
    public void liberarTareasDeUsuario(Long idUsuario, Long hogarId) {
        asignacionRepository.liberarTareasDeUsuario(idUsuario, hogarId);
    }
}
