package com.tareasdomesticas.hogar_service.auth.application.port.in;

import com.tareasdomesticas.hogar_service.auth.application.dto.RegistroResultDTO;

public interface RegistrarUsuarioUseCase {
    RegistroResultDTO registrar(RegistrarUsuarioCommand command);
}
