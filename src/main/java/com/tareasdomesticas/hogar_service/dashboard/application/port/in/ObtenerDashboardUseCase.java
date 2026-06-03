package com.tareasdomesticas.hogar_service.dashboard.application.port.in;

import com.tareasdomesticas.hogar_service.dashboard.application.dto.DashboardDTO;

public interface ObtenerDashboardUseCase {
    /**
     * @param idHogar         hogar a consultar
     * @param idUsuario       usuario que solicita (filtra si es miembro)
     * @param esAdministrador true → métricas de todo el hogar; false → solo tareas del usuario
     */
    DashboardDTO obtener(Long idHogar, Long idUsuario, boolean esAdministrador);
}
