package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;

public class InMemoryHogarRepository implements HogarRepository {
    private final Map<Integer, Hogar> hogares = new ConcurrentHashMap<>();

    public InMemoryHogarRepository() {
        cargarDatosDePrueba();
    }

    private void cargarDatosDePrueba() {
        Usuario admin = new Usuario(1, "Admin Prueba", "admin@prueba.com");
        Hogar hogar = new Hogar(9999, "Hogar de Prueba", "Hogar precargado para desarrollo", admin);
        hogar.agregarMiembro(new Usuario(2, "María García", "maria@prueba.com"));
        hogar.agregarMiembro(new Usuario(3, "Carlos López", "carlos@prueba.com"));
        hogares.put(hogar.getIdHogar(), hogar);
    }

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

    @Override
    public Optional<Hogar> buscarPorId(Long hogarId) {
        return Optional.ofNullable(hogares.get(hogarId.intValue()));
    }
}