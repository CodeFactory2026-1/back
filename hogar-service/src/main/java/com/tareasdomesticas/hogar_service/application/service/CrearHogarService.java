package com.tareasdomesticas.hogar_service.application.service;

import com.tareasdomesticas.hogar_service.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.domain.port.in.CrearHogarUseCase;
import com.tareasdomesticas.hogar_service.domain.port.out.HogarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CrearHogarService implements CrearHogarUseCase {
    private final HogarRepository hogarRepository;
    private static final Logger logger = LoggerFactory.getLogger(CrearHogarService.class);
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger((int) (System.currentTimeMillis() % 10000));

    public CrearHogarService(HogarRepository hogarRepository) {
        this.hogarRepository = hogarRepository;
    }

    @Override
    public Hogar crearHogar(String nombre, String descripcion, Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException("El usuario es requerido");
        }

        if (hogarRepository.buscarPorUsuarioId(usuario.getIdUsuario()).isPresent()) {
            throw new IllegalStateException("Ya hace parte de un hogar");
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
