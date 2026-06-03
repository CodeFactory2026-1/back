package com.tareasdomesticas.hogar_service.dashboard.infrastructure.adapter.in;

import com.tareasdomesticas.hogar_service.dashboard.application.dto.DashboardDTO;
import com.tareasdomesticas.hogar_service.dashboard.application.port.in.ObtenerDashboardUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    private final ObtenerDashboardUseCase obtenerDashboardUseCase;

    public DashboardController(ObtenerDashboardUseCase obtenerDashboardUseCase) {
        this.obtenerDashboardUseCase = obtenerDashboardUseCase;
    }

    /**
     * HU27 – Dashboard del hogar.
     *
     * GET /api/dashboard/hogares/{idHogar}?idUsuario=X&esAdministrador=true
     *
     * Admin → métricas de todo el hogar + carga de todos los miembros.
     * Miembro → métricas filtradas por sus propias tareas asignadas.
     */
    @GetMapping("/hogares/{idHogar}")
    public ResponseEntity<?> obtener(HttpServletRequest request,
            @PathVariable Long idHogar,
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
            DashboardDTO dashboard = obtenerDashboardUseCase.obtener(idHogar, idUsuario, esAdministrador);
            return ResponseEntity.ok(dashboard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al obtener dashboard", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }
}
