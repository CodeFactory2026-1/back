package com.tareasdomesticas.hogar_service.infrastructure.adapter.in;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tareasdomesticas.hogar_service.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.domain.port.in.CrearHogarUseCase;
import com.tareasdomesticas.hogar_service.infrastructure.adapter.in.DTO.CrearHogarRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> crearHogar(@RequestBody CrearHogarRequest request) {
        if (request == null) {
            logger.warn("Request para crear hogar es null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request inválido");
        }

        logger.info("Creando hogar: {} por usuario id={}", request.getNombreHogar(), request.getUsuarioId());
        Usuario usuario = new Usuario(
                request.getUsuarioId(),
                request.getNombreUsuario(),
                request.getCorreoUsuario());

        crearHogarUseCase.crearHogar(
                request.getNombreHogar(),
                request.getDescripcion(),
                usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Hogar creado exitosamente");
    }

}
