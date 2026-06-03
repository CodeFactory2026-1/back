package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out;

import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.tareas.application.port.out.ObtenerMiembrosHogarPort;

import java.util.List;

public class ObtenerMiembrosHogarAdapter implements ObtenerMiembrosHogarPort {

    private final HogarRepository hogarRepository;

    public ObtenerMiembrosHogarAdapter(HogarRepository hogarRepository) {
        this.hogarRepository = hogarRepository;
    }

    @Override
    public List<Long> obtenerIdsUsuarios(Long hogarId) {
        return hogarRepository.buscarPorId(hogarId)
                .map(h -> h.getIdsUsuarios())
                .orElse(List.of());
    }

    @Override
    public List<MiembroInfo> obtenerMiembros(Long hogarId) {
        return hogarRepository.buscarPorId(hogarId)
                .map(h -> h.getUsuarios().stream()
                        .filter(u -> u.getIdUsuario() != null)
                        .map(u -> new MiembroInfo(u.getIdUsuario(), u.getNombreUsuario()))
                        .toList())
                .orElse(List.of());
    }
}
