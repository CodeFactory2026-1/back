package com.tareasdomesticas.hogar_service.historial.domain.port.out;

import com.tareasdomesticas.hogar_service.historial.domain.model.EntradaHistorial;
import java.util.List;

public interface HistorialRepository {
    EntradaHistorial guardar(EntradaHistorial entrada);
    List<EntradaHistorial> listarPorHogar(Long idHogar);
    List<EntradaHistorial> listarPorUsuario(Long idHogar, Long idUsuario);
}
