package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.CrearHogarUseCase;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in.DTO.CrearHogarRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in.DTO.CrearHogarResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/hogares")
public class HogarController {
    private final CrearHogarUseCase crearHogarUseCase;
    private static final Logger logger = LoggerFactory.getLogger(HogarController.class);

    public HogarController(CrearHogarUseCase crearHogarUseCase) {
        this.crearHogarUseCase = crearHogarUseCase;
    }

    @PostMapping
    public ResponseEntity<CrearHogarResponse> crearHogar(@RequestBody CrearHogarRequest request) {
        if (request == null) {
            logger.warn("Request para crear hogar es null");
            CrearHogarResponse resp = new CrearHogarResponse(null, null, null, null, "Request inválido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        try {
            logger.info("Creando hogar: {} por usuario id={}", request.getNombreHogar(), request.getUsuarioId());

            Usuario usuario = new Usuario(
                    request.getUsuarioId(),
                    request.getNombreUsuario(),
                    request.getCorreoUsuario());

            Hogar hogarCreado = crearHogarUseCase.crearHogar(
                request.getNombreHogar(),
                request.getDescripcion(),
                usuario);

            CrearHogarResponse resp = new CrearHogarResponse(
                    hogarCreado.getIdHogar(), 
                    hogarCreado.getNombreHogar(),
                    hogarCreado.getDescripcionHogar(),
                    request.getUsuarioId(),
                    "Hogar creado exitosamente"
                );
                return ResponseEntity.status(HttpStatus.CREATED).body(resp);

        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación: {}", e.getMessage());
            CrearHogarResponse resp = new CrearHogarResponse(null, request != null ? request.getNombreHogar() : null,
                    request != null ? request.getDescripcion() : null, request != null ? request.getUsuarioId() : null,
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);

        } catch (IllegalStateException e) {
            // CA7 - usuario ya pertenece a un hogar
            logger.warn("Conflicto al crear hogar: {}", e.getMessage());
            CrearHogarResponse resp = new CrearHogarResponse(null, request != null ? request.getNombreHogar() : null,
                    request != null ? request.getDescripcion() : null, request != null ? request.getUsuarioId() : null,
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);

        } catch (Exception e) {
            logger.error("Error inesperado al crear hogar", e);
            CrearHogarResponse resp = new CrearHogarResponse(null, request != null ? request.getNombreHogar() : null,
                    request != null ? request.getDescripcion() : null, request != null ? request.getUsuarioId() : null,
                    "Algo salió mal, inténtelo de nuevo");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }
}