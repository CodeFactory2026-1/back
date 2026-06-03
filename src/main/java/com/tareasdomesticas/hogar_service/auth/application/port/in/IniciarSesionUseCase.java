package com.tareasdomesticas.hogar_service.auth.application.port.in;

import com.tareasdomesticas.hogar_service.auth.application.dto.LoginResultDTO;

public interface IniciarSesionUseCase {
    LoginResultDTO iniciarSesion(IniciarSesionCommand command);
}
