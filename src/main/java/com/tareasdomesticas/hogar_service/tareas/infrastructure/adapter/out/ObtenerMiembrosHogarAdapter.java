package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out;

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
                .map(hogar -> hogar.getIdsUsuarios())
                .orElse(List.of());
    }
}
