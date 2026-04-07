package com.tareasdomesticas.hogar_service.tareas.application.port.in;
 
import java.util.Map;
 
public interface AsignarTareaUseCase {
    Map<String, Object> asignarTareasSemanales(Long hogarId);
}
 