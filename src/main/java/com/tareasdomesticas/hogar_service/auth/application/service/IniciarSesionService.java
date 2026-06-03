package com.tareasdomesticas.hogar_service.auth.application.service;

import com.tareasdomesticas.hogar_service.auth.application.dto.LoginResultDTO;
import com.tareasdomesticas.hogar_service.auth.application.port.in.IniciarSesionCommand;
import com.tareasdomesticas.hogar_service.auth.application.port.in.IniciarSesionUseCase;
import com.tareasdomesticas.hogar_service.auth.domain.model.CuentaUsuario;
import com.tareasdomesticas.hogar_service.auth.domain.port.out.CuentaUsuarioRepository;
import com.tareasdomesticas.hogar_service.auth.domain.port.out.PasswordEncoderPort;
import com.tareasdomesticas.hogar_service.auth.domain.port.out.SesionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class IniciarSesionService implements IniciarSesionUseCase {

    private static final Logger log = LoggerFactory.getLogger(IniciarSesionService.class);

    private final CuentaUsuarioRepository cuentaRepo;
    private final PasswordEncoderPort     encoder;
    private final SesionRepository        sesionRepo;

    public IniciarSesionService(CuentaUsuarioRepository cuentaRepo,
                                PasswordEncoderPort encoder,
                                SesionRepository sesionRepo) {
        this.cuentaRepo = cuentaRepo;
        this.encoder    = encoder;
        this.sesionRepo = sesionRepo;
    }

    @Override
    public LoginResultDTO iniciarSesion(IniciarSesionCommand cmd) {
        if (cmd.correo() == null || cmd.correo().isBlank())
            throw new IllegalArgumentException("El correo es obligatorio.");
        if (cmd.contrasena() == null || cmd.contrasena().isBlank())
            throw new IllegalArgumentException("La contraseña es obligatoria.");

        CuentaUsuario cuenta = cuentaRepo.buscarPorCorreo(cmd.correo().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Correo o contraseña incorrectos."));

        if (!encoder.matches(cmd.contrasena(), cuenta.getHashContrasena()))
            throw new IllegalArgumentException("Correo o contraseña incorrectos.");

        String token = UUID.randomUUID().toString();

        // Persistir sesión con ip_origen y user_agent capturados del HTTP request
        sesionRepo.crearSesion(cuenta.getId(), token, cmd.ipOrigen(), cmd.userAgent());

        log.info("Sesión iniciada: correo={}, ip={}", cuenta.getCorreo(), cmd.ipOrigen());

        return new LoginResultDTO(cuenta.getId(), cuenta.getNombre(),
                cuenta.getCorreo(), cuenta.tieneHogar(), cuenta.getRol(), token);
    }
}
