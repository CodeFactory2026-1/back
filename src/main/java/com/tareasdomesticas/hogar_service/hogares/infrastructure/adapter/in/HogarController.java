package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in;

import com.tareasdomesticas.hogar_service.hogares.application.dto.CrearHogarResultDTO;
import com.tareasdomesticas.hogar_service.hogares.application.dto.MiembroDTO;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.*;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in.dto.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/hogares")
public class HogarController {

    private static final Logger log = LoggerFactory.getLogger(HogarController.class);

    private final CrearHogarUseCase crearHogarUseCase;
    private final AgregarMiembroUseCase agregarMiembroUseCase;
    private final EliminarMiembroUseCase eliminarMiembroUseCase;

    public HogarController(CrearHogarUseCase crearHogarUseCase,
            AgregarMiembroUseCase agregarMiembroUseCase,
            EliminarMiembroUseCase eliminarMiembroUseCase) {
        this.crearHogarUseCase = crearHogarUseCase;
        this.agregarMiembroUseCase = agregarMiembroUseCase;
        this.eliminarMiembroUseCase = eliminarMiembroUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crearHogar(HttpServletRequest request, @Valid @RequestBody CrearHogarRequest req) {
        try {
            Long idUsuarioAutenticado = (Long) request.getAttribute("idUsuario");
            if (idUsuarioAutenticado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "No autenticado. Inicie sesión para continuar."));
            }

            CrearHogarResultDTO hogar = crearHogarUseCase.crearHogar(new CrearHogarCommand(
                    idUsuarioAutenticado, req.getNombreUsuario(),
                    req.getCorreoUsuario(), req.getNombreHogar(), req.getDescripcion()));
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
    public ResponseEntity<?> agregarMiembro(HttpServletRequest request, @PathVariable Long hogarId,
            @Valid @RequestBody AgregarMiembroRequest req) {
        try {
            Long idUsuarioAutenticado = (Long) request.getAttribute("idUsuario");
            if (idUsuarioAutenticado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "No autenticado. Inicie sesión para continuar."));
            }
            MiembroDTO miembro = agregarMiembroUseCase.agregarMiembro(
                    new AgregarMiembroCommand(hogarId, idUsuarioAutenticado,
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
    public ResponseEntity<?> eliminarMiembro(HttpServletRequest request, @PathVariable Long hogarId,
            @Valid @RequestBody EliminarMiembroRequest req) {
        try {
            Long idUsuarioAutenticado = (Long) request.getAttribute("idUsuario");
            if (idUsuarioAutenticado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "No autenticado. Inicie sesión para continuar."));
            }
            eliminarMiembroUseCase.eliminarMiembro(
                    new EliminarMiembroCommand(hogarId, idUsuarioAutenticado,
                            req.getIdMiembro(), req.getNombreMiembro()));
            // HU6: "Miembro eliminado. [nombre] fue eliminado del hogar."
            String msg = "Miembro eliminado. " + req.getNombreMiembro() + " fue eliminado del hogar.";
            return ResponseEntity.ok(Map.of("mensaje", msg));
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
