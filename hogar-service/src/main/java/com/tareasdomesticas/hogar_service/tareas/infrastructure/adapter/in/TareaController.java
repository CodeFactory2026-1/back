package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tareasdomesticas.hogar_service.tareas.application.port.in.CrearTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.AsignarTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.Tarea;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in.DTO.CrearTareaResponse;

import jakarta.validation.Valid;

import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in.DTO.CrearTareaRequest;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {
    private final CrearTareaUseCase crearTareaUseCase;
    private final AsignarTareaUseCase asignarTareaUseCase;
    private final com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository tareaRepository;

    public TareaController(CrearTareaUseCase crearTareaUseCase,
            AsignarTareaUseCase asignarTareaUseCase,
            com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository tareaRepository) {
        this.crearTareaUseCase = crearTareaUseCase;
        this.asignarTareaUseCase = asignarTareaUseCase;
        this.tareaRepository = tareaRepository;
    }

    @PostMapping
    public ResponseEntity<?> crearTarea(@Valid @RequestBody CrearTareaRequest request) {
        try {
            Tarea tarea = crearTareaUseCase.crearTarea(
                    request.getHogarId(),
                    request.getNombre(),
                    request.getFoto(),
                    request.getFechaLimite(),
                    request.getDificultad(),
                    request.getPrioridad());
            return ResponseEntity.ok(
                    new CrearTareaResponse(
                            "Tarea creada exitosamente",
                            tarea.getIdTarea(),
                            tarea.getNombreTarea(),
                            tarea.getFotoTarea(),
                            tarea.getFechaLimite(),
                            tarea.getDificultad() != null ? tarea.getDificultad().name() : null,
                            tarea.getPrioridad() != null ? tarea.getPrioridad().name() : null,
                            tarea.getEstado() != null ? tarea.getEstado().name() : null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodasLasTareas() {
        List<Map<String, Object>> resultado = tareaRepository.listar()
                .stream()
                .map(t -> {
                    Map<String, Object> item = new java.util.LinkedHashMap<>();
                    item.put("idTarea", t.getIdTarea());
                    item.put("idHogar", t.getIdHogar());
                    item.put("nombre", t.getNombreTarea());
                    item.put("dificultad", t.getDificultad());
                    item.put("prioridad", t.getPrioridad());
                    item.put("estado", t.getEstado());
                    item.put("usuarioAsignado", t.getIdUsuarioAsignado() != null
                            ? t.getIdUsuarioAsignado()
                            : "Sin asignar");
                    item.put("fechaLimite", t.getFechaLimite());
                    return item;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/hogares/{hogarId}")
    public ResponseEntity<?> listarTareasPorHogar(@PathVariable Long hogarId) {
        List<Map<String, Object>> resultado = tareaRepository.listarPorHogar(hogarId)
                .stream()
                .map(t -> {
                    Map<String, Object> item = new java.util.LinkedHashMap<>();
                    item.put("idTarea", t.getIdTarea());
                    item.put("nombre", t.getNombreTarea());
                    item.put("dificultad", t.getDificultad());
                    item.put("prioridad", t.getPrioridad());
                    item.put("estado", t.getEstado());
                    item.put("usuarioAsignado", t.getIdUsuarioAsignado() != null
                            ? t.getIdUsuarioAsignado()
                            : "Sin asignar");
                    item.put("fechaLimite", t.getFechaLimite());
                    return item;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/hogares/{hogarId}/asignacion-semanal")
    public ResponseEntity<?> asignarTareas(@PathVariable Long hogarId) {
        try {
            Map<String, Object> resultado = asignarTareaUseCase.asignarTareasSemanales(hogarId);
            return ResponseEntity.ok(resultado);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Error inesperado: " + (e.getMessage() != null ? e.getMessage() : "Error desconocido")));
        }
    }

}