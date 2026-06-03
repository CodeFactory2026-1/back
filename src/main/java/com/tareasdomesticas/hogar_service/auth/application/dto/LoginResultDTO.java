package com.tareasdomesticas.hogar_service.auth.application.dto;

/**
 * Resultado de inicio de sesión.
 * tieneHogar dirige al frontend a la pantalla correcta (HU2).
 * token se usa para logout con invalidación real en tabla sesiones (HU3).
 * rol permite al frontend mostrar/ocultar opciones de menú (HU2).
 */
public record LoginResultDTO(Long idUsuario, String nombre, String correo,
                              boolean tieneHogar, String rol, String token) {}
