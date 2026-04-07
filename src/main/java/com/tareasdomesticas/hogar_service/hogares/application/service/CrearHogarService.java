package com.tareasdomesticas.hogar_service.hogares.application.service;

import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.CrearHogarUseCase;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CrearHogarService implements CrearHogarUseCase {
    private final HogarRepository hogarRepository;
    private static final Logger logger = LoggerFactory.getLogger(CrearHogarService.class);
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    public CrearHogarService(HogarRepository hogarRepository) {
        this.hogarRepository = hogarRepository;
    }

    @Override
    public Hogar crearHogar(String nombre, String descripcion, Usuario usuario) {

        if (usuario.getIdUsuario() == null) {
            throw new IllegalArgumentException("El usuario es requerido");
        }

        if (hogarRepository.buscarPorUsuarioId(usuario.getIdUsuario()).isPresent()) {
            throw new IllegalStateException("Ya hace parte de un hogar, por lo que no puede crear otro hogar");
        }

        try {
            Hogar hogar = new Hogar(
                    generarId(),
                    nombre,
                    descripcion,
                    usuario);
            logger.info("Administrador del hogar: {}", hogar.getAdministrador().getNombreUsuario());

            return hogarRepository.guardar(hogar);

        } catch (Exception e) {
            logger.error("Error al crear el hogar para el usuario {}", usuario.getIdUsuario(), e);
            throw new RuntimeException("Algo salió mal, inténtelo de nuevo", e);
        }

    }

    private Integer generarId() {
        return ID_GENERATOR.incrementAndGet();
    }
}