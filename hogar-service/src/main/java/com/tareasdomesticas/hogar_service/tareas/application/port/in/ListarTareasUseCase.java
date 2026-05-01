package com.tareasdomesticas.hogar_service.tareas.application.port.in;

import java.util.List;
import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;

public interface ListarTareasUseCase {
    List<TareaListadoDTO> listarTodas();
    List<TareaListadoDTO> listarPorHogar(Long hogarId);
}