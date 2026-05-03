package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;

import static java.lang.Integer.valueOf;

public class InMemoryHogarRepository implements HogarRepository {
    private final Map<Integer, Hogar> hogares = new ConcurrentHashMap<>();

    public InMemoryHogarRepository() {
        cargarDatosDePrueba();
    }

    private void cargarDatosDePrueba() {
        Usuario admin = new Usuario( 1L, "Admin Prueba", "admin@prueba.com");
        Hogar hogar = new Hogar(9999L, "Hogar de Prueba", "Hogar precargado para desarrollo", admin);
        hogar.agregarMiembro(new Usuario(2L, "María García", "maria@prueba.com"));
        hogar.agregarMiembro(new Usuario(3L, "Carlos López", "carlos@prueba.com"));
        hogares.put(Math.toIntExact(hogar.getIdHogar()), hogar);
    }

    @Override
    public Hogar guardar(Hogar hogar) {
        hogares.put(Math.toIntExact(hogar.getIdHogar()), hogar);
        return hogar;
    }

    @Override
    public Optional<Hogar> buscarPorUsuarioId(Long usuarioId) {
        return Optional.empty();
    }

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

    @Override
    public Optional<Hogar> buscarPorCorreoUsuario(String correo) {
        return Optional.empty();
    }
}