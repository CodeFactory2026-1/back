package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa;

import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.entity.HogarEntity;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.mapper.HogarMapper;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.repository.HogarJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaHogarRepositoryAdapter implements HogarRepository {

    private final HogarJpaRepository jpaRepository;

    @Override
    @Transactional
    public Hogar guardar(Hogar hogar) {
        HogarEntity entity = HogarMapper.toEntity(hogar);
        HogarEntity saved = jpaRepository.save(entity);
        return HogarMapper.toDomain(saved);
    }

    @Override
    public Optional<Hogar> buscarPorId(Long hogarId) {
        return jpaRepository.findById(hogarId)
                .map(HogarMapper::toDomain);
    }

    @Override
    public Optional<Hogar> buscarPorUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuariosIdUsuario(usuarioId)
                .map(HogarMapper::toDomain);
    }

    @Override
    public Optional<Hogar> buscarPorCorreoUsuario(String correo) {
        return jpaRepository.findByUsuariosCorreoUsuario(correo)
                .map(HogarMapper::toDomain);
    }
}
