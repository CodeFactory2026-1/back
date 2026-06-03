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

        // ── Responsabilidad exclusiva del servicio: verificar unicidad en repositorio.
        // Las validaciones de nombre y correo del usuario las hace el constructor de
        // Usuario
        // dentro de Hogar — no se duplican aquí.
        hogarRepository.buscarPorCorreoUsuario(command.correoUsuario())
                .ifPresent(h -> {
                    throw new IllegalStateException(
                            "Ya hace parte de un hogar, por lo que no puede crear otro hogar.");
                });

        // ── El constructor de Usuario valida: nombre no nulo/blank, longitud, correo
        // no nulo.
        // El constructor de Hogar valida: nombre 3-50 chars, descripción, creador no
        // nulo,
        // y llama a creador.convertirEnAdministrador().
        // El servicio no repite ninguna de esas validaciones.
        Usuario creador = new Usuario(
                command.usuarioId(),
                command.nombreUsuario(),
                command.correoUsuario());

        creador.convertirEnAdministrador();

        Hogar hogar = new Hogar(
                null,
                command.nombreHogar(),
                command.descripcion(),
                creador);

        Hogar guardado = hogarRepository.guardar(hogar);

        log.info("Hogar creado. ID={}, admin={}",
                guardado.getIdHogar(),
                guardado.getAdministrador().getNombreUsuario());

        return new CrearHogarResultDTO(
                guardado.getIdHogar(),
                guardado.getNombreHogar(),
                guardado.getDescripcionHogar(),
                guardado.getAdministrador().getIdUsuario());
    }
}
