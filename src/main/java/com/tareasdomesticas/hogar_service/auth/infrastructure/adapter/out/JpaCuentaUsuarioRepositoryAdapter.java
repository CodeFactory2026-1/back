package com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out;

import com.tareasdomesticas.hogar_service.auth.domain.model.CuentaUsuario;
import com.tareasdomesticas.hogar_service.auth.domain.port.out.CuentaUsuarioRepository;
import com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.entity.CuentaUsuarioEntity;
import com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.mapper.CuentaUsuarioMapper;
import com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.repository.CuentaUsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaCuentaUsuarioRepositoryAdapter implements CuentaUsuarioRepository {

    private final CuentaUsuarioJpaRepository jpaRepository;

    @Override
    @Transactional
    public CuentaUsuario guardar(CuentaUsuario cuenta) {
        CuentaUsuarioEntity entity = CuentaUsuarioMapper.toEntity(cuenta);
        return CuentaUsuarioMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<CuentaUsuario> buscarPorCorreo(String correo) {
        return jpaRepository.findByCorreo(correo).map(CuentaUsuarioMapper::toDomain);
    }

    @Override
    public boolean existePorCorreo(String correo) {
        return jpaRepository.existsByCorreo(correo);
    }
}
