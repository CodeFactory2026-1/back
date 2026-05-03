package com.tareasdomesticas.hogar_service.hogares.application.port.in;

import com.tareasdomesticas.hogar_service.hogares.application.dto.MiembroDTO;

public interface AgregarMiembroUseCase {
    MiembroDTO agregarMiembro(AgregarMiembroCommand command);
}
