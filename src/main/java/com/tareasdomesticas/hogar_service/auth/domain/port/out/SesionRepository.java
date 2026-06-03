package com.tareasdomesticas.hogar_service.auth.domain.port.out;

public interface SesionRepository {

    /** Crea sesión con token, ip y user-agent capturados del request HTTP. */
    void crearSesion(Long idUsuario, String token, String ipOrigen, String userAgent);

    void actualizarSesionMetadata(String token, String refreshToken,
            String ipOrigen, String userAgent);

    String obtenerRefreshTokenPorToken(String token);

    boolean invalidarSesion(String token);

    /** Verifica que el token exista, esté activo y no haya expirado. */
    boolean esSesionValida(String token);

    /**
     * Obtiene el id del usuario asociado al token si la sesión está activa y
     * vigente.
     */
    Long obtenerIdUsuarioPorToken(String token);
}
