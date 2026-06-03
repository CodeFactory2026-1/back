package com.tareasdomesticas.hogar_service.historial.infrastructure.adapter.in;

import com.tareasdomesticas.hogar_service.historial.application.dto.EntradaHistorialDTO;
import com.tareasdomesticas.hogar_service.historial.application.port.in.ConsultarHistorialUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    private static final Logger log = LoggerFactory.getLogger(HistorialController.class);

    private final ConsultarHistorialUseCase consultarHistorialUseCase;

    public HistorialController(ConsultarHistorialUseCase consultarHistorialUseCase) {
        this.consultarHistorialUseCase = consultarHistorialUseCase;
    }

    @GetMapping("/hogares/{idHogar}")
    public ResponseEntity<?> consultar(HttpServletRequest request, @PathVariable Long idHogar,
            @RequestParam Long idUsuario,
            @RequestParam(defaultValue = "false") boolean esAdministrador) {
        try {
            Long idUsuarioAutenticado = (Long) request.getAttribute("idUsuario");
            if (idUsuarioAutenticado == null) {
                return ResponseEntity.status(401).body(Map.of("mensaje", "No autenticado."));
            }
            if (!idUsuarioAutenticado.equals(idUsuario)) {
                return ResponseEntity.status(403).body(Map.of("mensaje", "Acceso denegado."));
            }
            List<EntradaHistorialDTO> resultado = consultarHistorialUseCase.consultar(idHogar, idUsuario,
                    esAdministrador);

            if (resultado.isEmpty()) {
                // HU26: mensajes distintos según rol
                String msg = esAdministrador
                        ? "Sin actividad registrada aún. Crea o modifica tareas para ver el historial."
                        : "Aún no tienes actividad registrada. Tu historial aparecerá cuando tengas tareas asignadas.";
                return ResponseEntity.ok(Map.of("mensaje", msg, "historial", resultado));
            }
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Error al consultar historial", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }
}
