package com.tareasdomesticas.hogar_service.hogares.domain.port.out;

import java.util.Optional;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;

public interface HogarRepository {
    Hogar guardar(Hogar hogar);
    Optional<Hogar> buscarPorUsuarioId(Long usuarioId);
    Optional<Hogar> buscarPorId(Long hogarId);
    Optional<Hogar> buscarPorCorreoUsuario(String correo);
}
