package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa;

import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.AsignacionSemanalRepository;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity.*;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.mapper.AsignacionMapper;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaAsignacionSemanalRepositoryAdapter implements AsignacionSemanalRepository {

    private final AsignacionSemanalJpaRepository      asignacionJpa;
    private final AsignacionSemanalTareaJpaRepository asignacionTareaJpa;

    @Override
    @Transactional
    public AsignacionSemanal guardarAsignacion(AsignacionSemanal asignacion) {
        AsignacionSemanalEntity entity = AsignacionMapper.toEntity(asignacion);
        AsignacionSemanalEntity saved  = asignacionJpa.save(entity);
        return AsignacionMapper.toDomain(saved);
    }

    @Override
    public Optional<AsignacionSemanal> obtenerUltimaAsignacion(Long hogarId) {
        return asignacionJpa
                .findTopByIdHogarOrderByFechaAsignacionDesc(hogarId)
                .map(AsignacionMapper::toDomain);
    }

    @Override
    @Transactional
    public AsignacionSemanalTarea guardarAsignacionTarea(AsignacionSemanalTarea ast) {
        AsignacionSemanalEntity asignacionRef =
                asignacionJpa.getReferenceById(ast.getIdAsignacion());
        AsignacionSemanalTareaEntity entity = AsignacionMapper.toEntity(ast, asignacionRef);
        AsignacionSemanalTareaEntity saved  = asignacionTareaJpa.save(entity);
        return AsignacionMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public AsignacionSemanalTarea actualizarAsignacionTarea(AsignacionSemanalTarea ast) {
        AsignacionSemanalTareaId id =
                new AsignacionSemanalTareaId(ast.getIdAsignacion(), ast.getIdTarea());
        if (!asignacionTareaJpa.existsById(id))
            throw new IllegalArgumentException("AsignacionSemanalTarea no encontrada.");
        return guardarAsignacionTarea(ast);
    }

    @Override
    public Optional<AsignacionSemanalTarea> buscarAsignacionTarea(Long idAsignacion, Long idTarea) {
        AsignacionSemanalTareaId id = new AsignacionSemanalTareaId(idAsignacion, idTarea);
        return asignacionTareaJpa.findById(id).map(AsignacionMapper::toDomain);
    }

    @Override
    public List<AsignacionSemanalTarea> listarPorAsignacion(Long idAsignacion) {
        return asignacionTareaJpa.findByIdIdAsignacion(idAsignacion).stream()
                .map(AsignacionMapper::toDomain)
                .toList();
    }

    @Override
    public List<AsignacionSemanalTarea> listarPorUsuario(Long idUsuario, Long idAsignacion) {
        return asignacionTareaJpa
                .findByIdIdAsignacionAndIdUsuarioAsignado(idAsignacion, idUsuario).stream()
                .map(AsignacionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<AsignacionSemanalTarea> buscarAsignacionActivaDeTarea(
            Long idTarea, Long hogarId) {
        return obtenerUltimaAsignacion(hogarId)
                .filter(AsignacionSemanal::perteneceASemanaActual)
                .flatMap(a -> buscarAsignacionTarea(a.getIdAsignacion(), idTarea));
    }

    @Override
    @Transactional
    public void liberarTareasDeUsuario(Long idUsuario, Long hogarId) {
        List<AsignacionSemanalTareaEntity> tareas =
                asignacionTareaJpa.findByUsuarioAndHogar(idUsuario, hogarId);

        for (AsignacionSemanalTareaEntity ast : tareas) {
            if (ast.getEstadoTarea() != EstadoTarea.FINALIZADO) {
                ast.setIdUsuarioAsignado(null);
                ast.setEstadoTarea(EstadoTarea.PENDIENTE);
                asignacionTareaJpa.save(ast);
            }
        }
    }

    @Override
    public List<Long> obtenerIdsTareasExcedentes(Long hogarId) {
        return asignacionTareaJpa.findExcedentesByHogar(hogarId).stream()
                .map(e -> e.getId().getIdTarea())
                .toList();
    }

    @Override
    public java.util.List<com.tareasdomesticas.hogar_service.tareas.domain.model.AsignacionSemanalTarea> listarAsignacionesActivasPorHogar(Long idHogar) {
        return asignacionTareaJpa.findActivasByHogar(idHogar).stream()
                .map(AsignacionMapper::toDomain)
                .toList();
    }
}
