package com.tareasdomesticas.hogar_service.historial.application.port.in;

import com.tareasdomesticas.hogar_service.historial.application.dto.EntradaHistorialDTO;
import java.util.List;

public interface ConsultarHistorialUseCase {
    /**
     * Devuelve el historial filtrado por rol:
     * - Administrador: todo el historial del hogar.
     * - Miembro normal: solo sus propias entradas.
     */
    List<EntradaHistorialDTO> consultar(Long idHogar, Long idUsuario, boolean esAdministrador);
}
