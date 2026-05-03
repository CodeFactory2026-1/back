package com.tareasdomesticas.hogar_service.hogares.application.service;

import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.hogares.application.dto.CrearHogarResultDTO;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.CrearHogarCommand;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.CrearHogarUseCase;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrearHogarService implements CrearHogarUseCase {

    private static final Logger log = LoggerFactory.getLogger(CrearHogarService.class);

    private final HogarRepository hogarRepository;

    public CrearHogarService(HogarRepository hogarRepository) {
        this.hogarRepository = hogarRepository;
    }

    @Override
    public CrearHogarResultDTO crearHogar(CrearHogarCommand command) {
        validarComando(command);

        hogarRepository.buscarPorCorreoUsuario(command.correoUsuario())
                .ifPresent(h -> {
                    throw new IllegalStateException(
                            "Ya hace parte de un hogar, por lo que no puede crear otro hogar.");
                });

        // El id se deja null: la BD genera el BIGSERIAL automáticamente
        Usuario creador = new Usuario(null, command.nombreUsuario(), command.correoUsuario());
        Hogar hogar     = new Hogar(null, command.nombreHogar(), command.descripcion(), creador);
        Hogar guardado  = hogarRepository.guardar(hogar);

        log.info("Hogar creado. ID={}, admin={}",
                guardado.getIdHogar(),
                guardado.getAdministrador().getNombreUsuario());

        return new CrearHogarResultDTO(
                guardado.getIdHogar(),
                guardado.getNombreHogar(),
                guardado.getDescripcionHogar(),
                guardado.getAdministrador().getIdUsuario());
    }

    private void validarComando(CrearHogarCommand command) {
        if (command.nombreUsuario() == null || command.nombreUsuario().isBlank())
            throw new IllegalArgumentException("El nombre del usuario es requerido.");
        if (command.correoUsuario() == null || command.correoUsuario().isBlank())
            throw new IllegalArgumentException("El correo del usuario es requerido.");
    }
}
