package com.tareasdomesticas.hogar_service.auth.domain.port.out;

/** Puerto de salida para el cifrado de contraseñas (ISP/DIP). */
public interface PasswordEncoderPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
