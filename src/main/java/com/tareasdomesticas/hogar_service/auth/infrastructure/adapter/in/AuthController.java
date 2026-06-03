package com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.in;

import com.tareasdomesticas.hogar_service.auth.application.dto.LoginResultDTO;
import com.tareasdomesticas.hogar_service.auth.application.dto.RegistroResultDTO;
import com.tareasdomesticas.hogar_service.auth.application.port.in.*;
import com.tareasdomesticas.hogar_service.auth.domain.port.out.SesionRepository;
import com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.in.dto.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private static final Pattern EMAIL_BASIC = Pattern.compile(
            "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$");

    private final RegistrarUsuarioUseCase registrarUseCase;
    private final IniciarSesionUseCase iniciarSesionUseCase;
    private final SesionRepository sesionRepository;

    public AuthController(RegistrarUsuarioUseCase registrarUseCase,
            IniciarSesionUseCase iniciarSesionUseCase,
            SesionRepository sesionRepository) {
        this.registrarUseCase = registrarUseCase;
        this.iniciarSesionUseCase = iniciarSesionUseCase;
        this.sesionRepository = sesionRepository;
    }

    /** HU1 – Registrar usuario */
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistrarUsuarioRequest req) {
        try {
            RegistroResultDTO result = registrarUseCase.registrar(
                    new RegistrarUsuarioCommand(
                            req.getNombre(), req.getCorreo(),
                            req.getContrasena(), req.getConfirmacionContrasena()));
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "mensaje",
                    "¡Cuenta creada! Tu cuenta fue registrada exitosamente. Ahora inicia sesión para continuar.",
                    "usuario", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al registrar usuario", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }

    /** HU2 – Iniciar sesión */
    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@Valid @RequestBody IniciarSesionRequest req,
            HttpServletRequest request) {
        try {
            if (req.getCorreo() != null && !req.getCorreo().isBlank()
                    && !EMAIL_BASIC.matcher(req.getCorreo().trim()).matches())
                return ResponseEntity.badRequest().body(Map.of("mensaje", "Formato inválido."));

            LoginResultDTO result = iniciarSesionUseCase.iniciarSesion(
                    new IniciarSesionCommand(
                            req.getCorreo(),
                            req.getContrasena(),
                            request.getRemoteAddr(),
                            request.getHeader("User-Agent")));

            // Persistir metadata de sesión (IP y User-Agent) en la tabla sesiones
            String ip = request.getRemoteAddr();
            String ua = request.getHeader("User-Agent");
            try {
                String existingRefresh = sesionRepository.obtenerRefreshTokenPorToken(result.token());
                if (existingRefresh == null || existingRefresh.isBlank()) {
                    existingRefresh = UUID.randomUUID().toString();
                }
                sesionRepository.actualizarSesionMetadata(result.token(), existingRefresh, ip, ua);
            } catch (Exception e) {
                log.warn("No se pudo actualizar metadata de sesión", e);
            }

            // HU3: el token se devuelve al cliente para que lo use en Authorization header
            return ResponseEntity.ok(Map.of(
                    "mensaje", "¡Bienvenido! " + result.nombre() + ", has iniciado sesión correctamente.",
                    "sesion", result));
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage().contains("Correo o contraseña")
                    ? "Credenciales incorrectas. El correo o la contraseña no son correctos."
                    : e.getMessage();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", msg));
        } catch (Exception e) {
            log.error("Error al iniciar sesión", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }

    /**
     * HU3 – Cerrar sesión.
     * El cliente envía el token en el header Authorization: Bearer <token>.
     * Se invalida en la tabla sesiones (activa = false).
     */
    @PostMapping("/logout")
    public ResponseEntity<?> cerrarSesion(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                sesionRepository.invalidarSesion(token);
            }
            return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada correctamente."));
        } catch (Exception e) {
            log.error("Error al cerrar sesión", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Algo salió mal, inténtelo de nuevo."));
        }
    }
}
