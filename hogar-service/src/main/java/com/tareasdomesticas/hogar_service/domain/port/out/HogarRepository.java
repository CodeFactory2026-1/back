package com.tareasdomesticas.hogar_service.domain.port.out;

import java.util.Optional;

import com.tareasdomesticas.hogar_service.domain.model.Hogar;

public interface HogarRepository {
    Hogar guardar(Hogar hogar);
    Optional<Hogar> buscarPorUsuarioId(Integer usuarioId);

}
