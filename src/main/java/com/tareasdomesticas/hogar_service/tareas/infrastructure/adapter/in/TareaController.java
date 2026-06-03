package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in;

import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tareasdomesticas.hogar_service.tareas.application.dto.AsignacionSemanalResponse;
import com.tareasdomesticas.hogar_service.tareas.application.dto.CrearTareaResultDTO;
import com.tareasdomesticas.hogar_service.tareas.application.dto.FiltroTareasDTO;
import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.*;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in.dto.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private static final Logger log = LoggerFactory.getLogger(TareaController.class);
    private final CrearTareaUseCase crearTareaUseCase;
    private final EditarTareaUseCase editarTareaUseCase;
    private final EliminarTareaUseCase eliminarTareaUseCase;
    private final CambiarEstadoTareaUseCase cambiarEstadoUseCase;
    private final AsignarTareaUseCase asignarTareaUseCase;
    private final ListarTareasUseCase listarTareasUseCase;
    private final FiltrarTareasUseCase filtrarTareasUseCase;

    public TareaController(CrearTareaUseCase crearTareaUseCase,
            EditarTareaUseCase editarTareaUseCase,
            EliminarTareaUseCase eliminarTareaUseCase,
            CambiarEstadoTareaUseCase cambiarEstadoUseCase,
            AsignarTareaUseCase asignarTareaUseCase,
            ListarTareasUseCase listarTareasUseCase,
            FiltrarTareasUseCase filtrarTareasUseCase) {
        this.crearTareaUseCase = crearTareaUseCase;
        this.editarTareaUseCase = editarTareaUseCase;
        this.eliminarTareaUseCase = eliminarTareaUseCase;
        this.cambiarEstadoUseCase = cambiarEstadoUseCase;
        this.asignarTareaUseCase = asignarTareaUseCase;
        this.listarTareasUseCase = listarTareasUseCase;
        this.filtrarTareasUseCase = filtrarTareasUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crearTarea(HttpServletRequest request, @Valid @RequestBody CrearTareaRequest req) {
        try {
            Long idUsuarioAutenticado = (Long) request.getAttribute("idUsuario");
            if (idUsuarioAutenticado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "No autenticado. Inicie sesión para continuar."));
            }

            CrearTareaResultDTO r = crearTareaUseCase.crearTarea(
                    new CrearTareaCommand(
                            req.getIdHogar(),
                            idUsuarioAutenticado,
                            req.getNombre(),
                            req.getDescripcion(),
                            req.getFoto(),
                            req.getFechaLimite(),
                            req.getDificultad(),
                            req.getPrioridad()));
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Tarea creada exitosamente",
                    "id", r.getIdTarea(), "nombre", r.getNombreTarea(),
                    "estado", r.getEstado()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }

    @PutMapping("/{idTarea}")
    public ResponseEntity<?> editarTarea(HttpServletRequest request, @PathVariable Long idTarea,
            @Valid @RequestBody EditarTareaRequest req) {
        try {
            Long idUsuarioAutenticado = (Long) request.getAttribute("idUsuario");
            if (idUsuarioAutenticado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "No autenticado. Inicie sesión para continuar."));
            }

            TareaListadoDTO result = editarTareaUseCase.editarTarea(
                    new EditarTareaCommand(idTarea, idUsuarioAutenticado, req.getNombre(), req.getDescripcion(),
                            req.getDificultad(), req.getFechaLimite()));
            return ResponseEntity.ok(Map.of("mensaje", "Tarea actualizada exitosamente", "tarea", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }

    @DeleteMapping("/{idTarea}")
    public ResponseEntity<?> eliminarTarea(HttpServletRequest request, @PathVariable Long idTarea) {
        try {
            Long idUsuarioAutenticado = (Long) request.getAttribute("idUsuario");
            if (idUsuarioAutenticado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "No autenticado. Inicie sesión para continuar."));
            }
            eliminarTareaUseCase.eliminarTarea(idTarea, idUsuarioAutenticado);
            return ResponseEntity.ok(Map.of("mensaje", "Tarea eliminada exitosamente."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }

    @PatchMapping("/{idTarea}/estado")
    public ResponseEntity<?> cambiarEstado(HttpServletRequest request, @PathVariable Long idTarea,
            @Valid @RequestBody CambiarEstadoRequest req) {
        try {
            Long idUsuarioAutenticado = (Long) request.getAttribute("idUsuario");
            if (idUsuarioAutenticado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "No autenticado. Inicie sesión para continuar."));
            }

            TareaListadoDTO result = cambiarEstadoUseCase.cambiarEstado(
                    new CambiarEstadoCommand(idTarea, req.getNuevoEstado(), idUsuarioAutenticado));
            return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado correctamente.", "tarea", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodas() {
        return ResponseEntity.ok(listarTareasUseCase.listarTodas());
    }

    @GetMapping("/hogares/{hogarId}")
    public ResponseEntity<?> listarPorHogar(@PathVariable Long hogarId) {
        return ResponseEntity.ok(listarTareasUseCase.listarPorHogar(hogarId));
    }

    @GetMapping("/hogares/{hogarId}/filtrar")
    public ResponseEntity<?> filtrar(@PathVariable Long hogarId,
            @ModelAttribute FiltrarTareasRequest req) {
        try {
            List<TareaListadoDTO> resultado = filtrarTareasUseCase.filtrar(
                    new FiltroTareasDTO(
                            hogarId,
                            req.getEstado(),
                            req.getIdUsuario(),
                            req.getPrioridad(),
                            req.getDificultad(),
                            req.getNombre()));

            if (resultado.isEmpty())
                return ResponseEntity.ok(
                        Map.of("mensaje", "No se encontraron tareas con los filtros aplicados.", "tareas", resultado));
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al filtrar tareas", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }

    @PostMapping("/hogares/{hogarId}/asignacion-semanal")
    public ResponseEntity<?> asignarTareas(@PathVariable Long hogarId) {
        try {
            AsignacionSemanalResponse resultado = asignarTareaUseCase.asignarTareasSemanales(hogarId);
            return ResponseEntity.ok(resultado);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Algo salió mal, inténtelo de nuevo."));
        }
    }

}