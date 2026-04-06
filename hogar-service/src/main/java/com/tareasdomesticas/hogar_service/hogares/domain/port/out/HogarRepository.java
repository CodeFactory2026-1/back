package com.tareasdomesticas.hogar_service.hogares.domain.port.out;

import java.util.Optional;

import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;

public interface HogarRepository {
    Hogar guardar(Hogar hogar);
    Optional<Hogar> buscarPorUsuarioId(Integer usuarioId);
    Optional<Hogar> buscarPorId(Long hogarId);

}
