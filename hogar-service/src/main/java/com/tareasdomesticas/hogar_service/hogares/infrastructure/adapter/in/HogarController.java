package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.tareasdomesticas.hogar_service.hogares.application.dto.CrearHogarResultDTO;
import com.tareasdomesticas.hogar_service.hogares.application.dto.MiembroDTO;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.*;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in.dto.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hogares")
public class HogarController {

    private static final Logger log = LoggerFactory.getLogger(HogarController.class);

    private final CrearHogarUseCase     crearHogarUseCase;
    private final AgregarMiembroUseCase agregarMiembroUseCase;
    private final EliminarMiembroUseCase eliminarMiembroUseCase;

    public HogarController(CrearHogarUseCase crearHogarUseCase,
            AgregarMiembroUseCase agregarMiembroUseCase,
            EliminarMiembroUseCase eliminarMiembroUseCase) {
        this.crearHogarUseCase     = crearHogarUseCase;
        this.agregarMiembroUseCase = agregarMiembroUseCase;
        this.eliminarMiembroUseCase = eliminarMiembroUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crearHogar(@Valid @RequestBody CrearHogarRequest req) {
        try {
            log.info("Creando hogar: {} por usuario correo={}", req.getNombreHogar(), req.getCorreoUsuario());
            CrearHogarCommand command = new CrearHogarCommand(
                    req.getUsuarioId(),
                    req.getNombreUsuario(),
                    req.getCorreoUsuario(),
                    req.getNombreHogar(),
                    req.getDescripcion());
            CrearHogarResultDTO hogar = crearHogarUseCase.crearHogar(command);
            return ResponseEntity.status(HttpStatus.CREATED).body(new CrearHogarResponse(
                    hogar.idHogar(), hogar.nombreHogar(), hogar.descripcionHogar(),
                    hogar.idAdministrador(), "Hogar creado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al crear hogar", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }

    @PostMapping("/{hogarId}/miembros")
    public ResponseEntity<?> agregarMiembro(@PathVariable Long hogarId,
            @Valid @RequestBody AgregarMiembroRequest req) {
        try {
            MiembroDTO miembro = agregarMiembroUseCase.agregarMiembro(
                    new AgregarMiembroCommand(hogarId, req.getIdAdministrador(),
                            req.getNombre(), req.getCorreo()));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("mensaje", "Miembro agregado exitosamente.", "miembro", miembro));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al agregar miembro", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }

    @DeleteMapping("/{hogarId}/miembros")
    public ResponseEntity<?> eliminarMiembro(@PathVariable Long hogarId,
            @Valid @RequestBody EliminarMiembroRequest req) {
        try {
            eliminarMiembroUseCase.eliminarMiembro(
                    new EliminarMiembroCommand(hogarId, req.getIdAdministrador(), req.getIdMiembro()));
            return ResponseEntity.ok(
                    Map.of("mensaje", "Miembro eliminado. Sus tareas quedaron pendientes."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al eliminar miembro", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }
}
