package com.tareasdomesticas.hogar_service.historial.application.service;

import com.tareasdomesticas.hogar_service.historial.application.dto.EntradaHistorialDTO;
import com.tareasdomesticas.hogar_service.historial.application.port.in.ConsultarHistorialUseCase;
import com.tareasdomesticas.hogar_service.historial.application.port.in.RegistrarAccionHistorialUseCase;
import com.tareasdomesticas.hogar_service.historial.domain.model.EntradaHistorial;
import com.tareasdomesticas.hogar_service.historial.domain.model.TipoAccion;
import com.tareasdomesticas.hogar_service.historial.domain.port.out.HistorialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementa ambos puertos de entrada.
 * Cumple ISP: los clientes que solo registran no necesitan exponer la consulta.
 */
public class HistorialService implements ConsultarHistorialUseCase,
        RegistrarAccionHistorialUseCase {

    private static final Logger log = LoggerFactory.getLogger(HistorialService.class);

    private final HistorialRepository historialRepo;

    public HistorialService(HistorialRepository historialRepo) {
        this.historialRepo = historialRepo;
    }

    @Override
    public void registrar(Long idHogar, Long idTarea, String nombreTarea,
            TipoAccion tipoAccion, Long idUsuarioActor,
            String nombreUsuarioActor, String detalle) {
        EntradaHistorial entrada = new EntradaHistorial(
                null, idHogar, idTarea, nombreTarea,
                tipoAccion, idUsuarioActor, nombreUsuarioActor, LocalDateTime.now(), detalle);
        historialRepo.guardar(entrada);
        log.info("Historial registrado: hogar={}, tarea={}, accion={}", idHogar, idTarea, tipoAccion);
    }

    @Override
    public List<EntradaHistorialDTO> consultar(Long idHogar, Long idUsuario,
            boolean esAdministrador) {
        List<EntradaHistorial> entradas = esAdministrador
                ? historialRepo.listarPorHogar(idHogar)
                : historialRepo.listarPorUsuario(idHogar, idUsuario);
        return entradas.stream().map(this::toDTO).toList();
    }

    private EntradaHistorialDTO toDTO(EntradaHistorial e) {
        return new EntradaHistorialDTO(
                e.getId(), e.getIdTarea(), e.getNombreTarea(),
                e.getTipoAccion().name(),
                e.getIdUsuarioActor(), e.getNombreUsuarioActor(),
                e.getFechaHora(), e.getDetalle());
    }
}
