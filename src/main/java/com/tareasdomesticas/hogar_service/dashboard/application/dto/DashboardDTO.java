package com.tareasdomesticas.hogar_service.dashboard.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DashboardDTO(
        long   totalTareas,
        long   tareasPendientes,
        long   tareasPospuestas,      // excedente=true de la semana anterior
        long   tareasAsignadas,
        long   tareasEnProceso,
        long   tareasFinalizadas,
        double porcentajeCumplimientoSemanal,
        List<CargaMiembroDTO>        cargaPorMiembro,
        List<ProximoVencimientoDTO>  proximosVencimientos
) {
    public static final int MAX_PUNTOS_SEMANA = 6;

    public record CargaMiembroDTO(
            Long   idUsuario,
            String nombreUsuario,
            long   cantidadTareas,
            int    maxPuntos
    ) {
        public CargaMiembroDTO(Long idUsuario, String nombreUsuario, long cantidadTareas) {
            this(idUsuario, nombreUsuario, cantidadTareas, MAX_PUNTOS_SEMANA);
        }
    }

    public record ProximoVencimientoDTO(
            Long          idTarea,
            String        nombreTarea,
            LocalDateTime fechaLimite,
            long          diasRestantes,
            boolean       vencida,
            boolean       resaltada
    ) {}
}
