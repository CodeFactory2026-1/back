package com.tareasdomesticas.hogar_service.hogares.application.service;

import com.tareasdomesticas.hogar_service.auth.domain.port.out.CuentaUsuarioRepository;
import com.tareasdomesticas.hogar_service.common.application.port.out.ResolverNombreUsuarioPort;
import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.historial.application.port.in.RegistrarAccionHistorialUseCase;
import com.tareasdomesticas.hogar_service.historial.domain.model.TipoAccion;
import com.tareasdomesticas.hogar_service.hogares.application.dto.MiembroDTO;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.AgregarMiembroCommand;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.AgregarMiembroUseCase;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;

public class AgregarMiembroService implements AgregarMiembroUseCase {

        private final HogarRepository hogarRepository;
        private final CuentaUsuarioRepository cuentaUsuarioRepository;
        private final RegistrarAccionHistorialUseCase historial;
        private final ResolverNombreUsuarioPort resolverNombre;

        public AgregarMiembroService(HogarRepository hogarRepository,
                        CuentaUsuarioRepository cuentaUsuarioRepository,
                        RegistrarAccionHistorialUseCase historial,
                        ResolverNombreUsuarioPort resolverNombre) {
                this.hogarRepository = hogarRepository;
                this.cuentaUsuarioRepository = cuentaUsuarioRepository;
                this.historial = historial;
                this.resolverNombre = resolverNombre;
        }

        @Override
        public MiembroDTO agregarMiembro(AgregarMiembroCommand command) {
                Hogar hogar = hogarRepository.buscarPorId(command.hogarId())
                                .orElseThrow(() -> new IllegalArgumentException("El hogar no existe."));

                if (!hogar.getAdministrador().getIdUsuario().equals(command.idAdministrador()))
                        throw new IllegalStateException("Solo el administrador puede agregar miembros.");

                var cuenta = cuentaUsuarioRepository
                                .buscarPorCorreo(command.correo())
                                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

                Usuario usuario = new Usuario(cuenta.getId(), cuenta.getNombre(), cuenta.getCorreo());
                hogar.agregarMiembro(usuario);
                Hogar guardado = hogarRepository.guardar(hogar);

                Usuario agregado = guardado.getUsuarios().stream()
                                .filter(u -> u.getCorreoUsuario().equalsIgnoreCase(command.correo()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException(
                                                "No se encontró el miembro recién agregado."));

                // Actor = administrador que realizó la acción
                String nombreAdmin = resolverNombre.resolverNombre(command.idAdministrador());
                String detalle = "Miembro agregado: " + agregado.getCorreoUsuario();
                historial.registrar(command.hogarId(), null, null,
                                TipoAccion.MIEMBRO_AGREGADO,
                                command.idAdministrador(), nombreAdmin, detalle);

                return new MiembroDTO(
                                agregado.getIdUsuario(),
                                agregado.getNombreUsuario(),
                                agregado.getCorreoUsuario(),
                                agregado.getRolUsuario().name());
        }
}
