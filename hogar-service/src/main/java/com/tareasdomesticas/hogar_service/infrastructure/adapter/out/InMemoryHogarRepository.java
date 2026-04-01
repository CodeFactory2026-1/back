package com.tareasdomesticas.hogar_service.infrastructure.adapter.out;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.tareasdomesticas.hogar_service.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.domain.port.out.HogarRepository;

public class InMemoryHogarRepository implements HogarRepository  {
    private final Map<Integer, Hogar> hogares = new HashMap<>();

    @Override
    public Hogar guardar(Hogar hogar) {
        hogares.put(hogar.getIdHogar(), hogar);
        return hogar;
    }

    @Override
    public Optional<Hogar> buscarPorUsuarioId(Integer usuarioId) {
        return hogares.values().stream()
                .filter(h -> h.getUsuarios().stream()
                        .anyMatch(u -> u.getIdUsuario().equals(usuarioId)))
                .findFirst();
    }
}
