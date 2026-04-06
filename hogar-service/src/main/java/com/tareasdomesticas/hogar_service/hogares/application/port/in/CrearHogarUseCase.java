package com.tareasdomesticas.hogar_service.hogares.application.port.in;

import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
public interface CrearHogarUseCase {
    Hogar crearHogar(String nombre, String descripcion, Usuario usuario);
}
