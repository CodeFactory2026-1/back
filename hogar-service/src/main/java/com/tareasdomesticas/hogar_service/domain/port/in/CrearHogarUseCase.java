package com.tareasdomesticas.hogar_service.domain.port.in;

import com.tareasdomesticas.hogar_service.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.domain.model.Usuario;

public interface CrearHogarUseCase {
    Hogar crearHogar(String nombre, String descripcion, Usuario usuario);
}
