package com.tareasdomesticas.hogar_service.invitaciones.infrastructure.adapter.in;

import com.tareasdomesticas.hogar_service.invitaciones.application.dto.InvitacionDTO;
import com.tareasdomesticas.hogar_service.invitaciones.application.port.in.*;
import com.tareasdomesticas.hogar_service.invitaciones.infrastructure.adapter.in.dto.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invitaciones")
public class InvitacionController {

    private static final Logger log = LoggerFactory.getLogger(InvitacionController.class);

    private final EnviarInvitacionUseCase enviarUseCase;
    private final ResponderInvitacionUseCase responderUseCase;
    private final ListarInvitacionesPendientesUseCase listarUseCase;

    public InvitacionController(EnviarInvitacionUseCase enviarUseCase,
            ResponderInvitacionUseCase responderUseCase,
            ListarInvitacionesPendientesUseCase listarUseCase) {
        this.enviarUseCase = enviarUseCase;
        this.responderUseCase = responderUseCase;
        this.listarUseCase = listarUseCase;
    }

    @PostMapping("/hogares/{idHogar}")
    public ResponseEntity<?> enviar(HttpServletRequest request, @PathVariable Long idHogar,
            @Valid @RequestBody EnviarInvitacionRequest req) {
        try {
            Long idUsuarioAutenticado = (Long) request.getAttribute("idUsuario");
            if (idUsuarioAutenticado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "No autenticado. Inicie sesión para continuar."));
            }

            InvitacionDTO inv = enviarUseCase.enviar(
                    new EnviarInvitacionCommand(idHogar, idUsuarioAutenticado,
                            req.getNombre(), req.getCorreo()));
            // HU6: "Invitación enviada. Se envió la invitación a [nombre] ([correo]).
            // Cuando inicie sesión podrá aceptarla."
            String msg = "Invitación enviada. Se envió la invitación a "
                    + inv.nombreInvitado() + " (" + inv.correoInvitado()
                    + "). Cuando inicie sesión podrá aceptarla.";
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("mensaje", msg, "invitacion", inv));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al enviar invitación", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }

    @PatchMapping("/{idInvitacion}/respuesta")
    public ResponseEntity<?> responder(@PathVariable Long idInvitacion,
            @RequestBody ResponderInvitacionRequest req) {
        try {
            InvitacionDTO inv = responderUseCase.responder(
                    new ResponderInvitacionCommand(idInvitacion, req.isAceptar()));
            // HU6: "¡Te uniste al hogar! Ahora eres miembro de [nombre del hogar]."
            // El nombre del hogar viene en el DTO; si no está disponible se usa el id
            String msg = req.isAceptar()
                    ? "¡Te uniste al hogar! Ahora eres miembro del hogar."
                    : "Invitación rechazada.";
            return ResponseEntity.ok(Map.of("mensaje", msg, "invitacion", inv));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al responder invitación", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }

    @GetMapping
    public ResponseEntity<?> listarPendientes(@RequestParam String correo) {
        try {
            List<InvitacionDTO> invitaciones = listarUseCase.listarParaUsuario(correo);
            return ResponseEntity.ok(invitaciones);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al listar invitaciones", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }
}
