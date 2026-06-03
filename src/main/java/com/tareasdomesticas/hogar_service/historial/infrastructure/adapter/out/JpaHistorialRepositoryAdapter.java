package com.tareasdomesticas.hogar_service.historial.infrastructure.adapter.out;

import com.tareasdomesticas.hogar_service.historial.domain.model.EntradaHistorial;
import com.tareasdomesticas.hogar_service.historial.domain.port.out.HistorialRepository;
import com.tareasdomesticas.hogar_service.historial.infrastructure.adapter.out.jpa.mapper.HistorialMapper;
import com.tareasdomesticas.hogar_service.historial.infrastructure.adapter.out.jpa.repository.HistorialJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaHistorialRepositoryAdapter implements HistorialRepository {

    private final HistorialJpaRepository jpaRepository;

    @Override
    @Transactional
    public EntradaHistorial guardar(EntradaHistorial entrada) {
        return HistorialMapper.toDomain(jpaRepository.save(HistorialMapper.toEntity(entrada)));
    }

    @Override
    public List<EntradaHistorial> listarPorHogar(Long idHogar) {
        return jpaRepository.findByIdHogarOrderByFechaHoraDesc(idHogar)
                .stream().map(HistorialMapper::toDomain).toList();
    }

    @Override
    public List<EntradaHistorial> listarPorUsuario(Long idHogar, Long idUsuario) {
        return jpaRepository.findByIdHogarAndIdUsuarioActorOrderByFechaHoraDesc(idHogar, idUsuario)
                .stream().map(HistorialMapper::toDomain).toList();
    }
}
