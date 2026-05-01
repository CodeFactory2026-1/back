package com.tareasdomesticas.hogar_service.tareas.application.port.in;

import java.util.List;
import com.tareasdomesticas.hogar_service.tareas.application.dto.FiltroTareasDTO;
import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;


public interface FiltrarTareasUseCase {
    List<TareaListadoDTO> filtrar(FiltroTareasDTO filtro);
}
