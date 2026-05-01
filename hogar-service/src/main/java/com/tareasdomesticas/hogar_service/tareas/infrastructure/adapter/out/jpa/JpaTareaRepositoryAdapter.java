package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa;

import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity.TareaEntity;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.mapper.TareaMapper;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.repository.TareaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaTareaRepositoryAdapter implements TareaRepository {

    private final TareaJpaRepository jpaRepository;

    @Override
    @Transactional
    public Tarea guardar(Tarea tarea) {
        TareaEntity saved = jpaRepository.save(TareaMapper.toEntity(tarea));
        return TareaMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Tarea actualizar(Tarea tarea) {
        if (!jpaRepository.existsById(tarea.getIdTarea()))
            throw new IllegalArgumentException("La tarea no existe.");
        TareaEntity saved = jpaRepository.save(TareaMapper.toEntity(tarea));
        return TareaMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void eliminar(Long idTarea) {
        if (!jpaRepository.existsById(idTarea))
            throw new IllegalArgumentException("La tarea no existe.");
        jpaRepository.deleteById(idTarea);
    }

    @Override
    public Optional<Tarea> buscarPorId(Long idTarea) {
        return jpaRepository.findById(idTarea).map(TareaMapper::toDomain);
    }

    @Override
    public List<Tarea> listar() {
        return jpaRepository.findAll().stream()
                .map(TareaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Tarea> listarPorHogar(Long hogarId) {
        return jpaRepository.findByIdHogar(hogarId).stream()
                .map(TareaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Tarea> listarPendientesPorHogar(Long hogarId, List<Long> idsYaAsignadas) {
        if (idsYaAsignadas == null || idsYaAsignadas.isEmpty()) {
            return listarPorHogar(hogarId);
        }
        return jpaRepository.findPendientesByHogar(hogarId, idsYaAsignadas).stream()
                .map(TareaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Tarea> filtrar(Long hogarId, String nombre,
                               DificultadTarea dificultad, PrioridadTarea prioridad) {
        return jpaRepository.findByIdHogar(hogarId).stream()
            .filter(t -> dificultad == null || t.getDificultadTarea() == dificultad)
            .filter(t -> prioridad  == null || t.getPrioridadTarea()  == prioridad)
            .filter(t -> nombre == null || nombre.isBlank()
                    || t.getNombreTarea().toLowerCase()
                            .contains(nombre.toLowerCase()))
            .map(TareaMapper::toDomain)
            .toList();
    }
    @Override
    public boolean existeTareaConMismoNombreEnSemana(
            String nombre, LocalDateTime fechaLimite, Long hogarId) {
        int semanaObjetivo = fechaLimite.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int anioObjetivo   = fechaLimite.getYear();
        return jpaRepository.findByIdHogar(hogarId).stream()
                .anyMatch(t -> t.getNombreTarea().equalsIgnoreCase(nombre)
                        && t.getFechaLimite().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == semanaObjetivo
                        && t.getFechaLimite().getYear() == anioObjetivo);
    }
}
