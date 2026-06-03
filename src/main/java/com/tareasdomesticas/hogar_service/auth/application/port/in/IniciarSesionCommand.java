package com.tareasdomesticas.hogar_service.auth.application.port.in;

public record IniciarSesionCommand(
    String correo,
    String contrasena,
    String ipOrigen,     // capturada desde HttpServletRequest en el controller
    String userAgent     // capturada desde el header User-Agent
) {}
