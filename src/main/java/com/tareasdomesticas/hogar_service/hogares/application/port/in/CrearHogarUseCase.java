package com.tareasdomesticas.hogar_service.hogares.application.port.in;

import com.tareasdomesticas.hogar_service.hogares.application.dto.CrearHogarResultDTO;
public interface CrearHogarUseCase {
    CrearHogarResultDTO crearHogar(CrearHogarCommand command);
}