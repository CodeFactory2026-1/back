package com.tareasdomesticas.hogar_service.auth.application.service;

import com.tareasdomesticas.hogar_service.auth.application.dto.RegistroResultDTO;
import com.tareasdomesticas.hogar_service.auth.application.port.in.RegistrarUsuarioCommand;
import com.tareasdomesticas.hogar_service.auth.application.port.in.RegistrarUsuarioUseCase;
import com.tareasdomesticas.hogar_service.auth.domain.model.CuentaUsuario;
import com.tareasdomesticas.hogar_service.auth.domain.port.out.CuentaUsuarioRepository;
import com.tareasdomesticas.hogar_service.auth.domain.port.out.PasswordEncoderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrarUsuarioService implements RegistrarUsuarioUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegistrarUsuarioService.class);
    private static final int MIN_PASS = 6;

    private final CuentaUsuarioRepository cuentaRepo;
    private final PasswordEncoderPort     encoder;

    public RegistrarUsuarioService(CuentaUsuarioRepository cuentaRepo,
                                   PasswordEncoderPort encoder) {
        this.cuentaRepo = cuentaRepo;
        this.encoder    = encoder;
    }

    @Override
    public RegistroResultDTO registrar(RegistrarUsuarioCommand cmd) {

        // HU1: "Contraseña obligatoria (mín. 6 caracteres)."
        if (cmd.contrasena() == null || cmd.contrasena().length() < MIN_PASS)
            throw new IllegalArgumentException("Contraseña obligatoria (mín. 6 caracteres).");

        // HU1: "Las contraseñas no coinciden."
        if (!cmd.contrasena().equals(cmd.confirmacionContrasena()))
            throw new IllegalArgumentException("Las contraseñas no coinciden.");

        // HU1: "Este correo ya está registrado."
        if (cuentaRepo.existePorCorreo(cmd.correo().trim().toLowerCase()))
            throw new IllegalStateException("Este correo ya está registrado.");

        // El constructor de CuentaUsuario valida nombre y formato de correo con mensajes HU
        String hash    = encoder.encode(cmd.contrasena());
        CuentaUsuario cuenta   = new CuentaUsuario(null, cmd.nombre(), cmd.correo(), hash, null);
        CuentaUsuario guardada = cuentaRepo.guardar(cuenta);

        log.info("Usuario registrado: correo={}", guardada.getCorreo());
        return new RegistroResultDTO(guardada.getId(), guardada.getNombre(), guardada.getCorreo());
    }
}
