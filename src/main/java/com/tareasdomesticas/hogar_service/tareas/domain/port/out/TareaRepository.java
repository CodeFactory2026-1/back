package com.tareasdomesticas.hogar_service.tareas.domain.port.out;
 
import java.time.LocalDateTime;
import java.util.List;
 
import com.tareasdomesticas.hogar_service.tareas.domain.model.Tarea;
 
public interface TareaRepository {
    Tarea guardar(Tarea tarea);
 
    List<Tarea> listar();
    List<Tarea> listarPorHogar(Long hogarId);
    List<Tarea> listarPendientesPorHogar(Long hogarId);
    boolean existeTareaConMismoNombreEnSemana(String nombre, LocalDateTime fechaLimite, Long hogarId);
 
}
 