package com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out;

import com.tareasdomesticas.hogar_service.auth.domain.port.out.SesionRepository;
import com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.entity.SesionEntity;
import com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.repository.SesionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JpaSesionRepositoryAdapter implements SesionRepository {

    private final SesionJpaRepository jpaRepository;

    @Override
    @Transactional
    public void crearSesion(Long idUsuario, String token, String ipOrigen, String userAgent) {
        SesionEntity sesion = SesionEntity.builder()
                .idUsuario(idUsuario)
                .token(token)
                .ipOrigen(ipOrigen) // capturado del HTTP request
                .userAgent(userAgent) // capturado del header User-Agent
                .fechaInicio(LocalDateTime.now())
                .fechaExpiracion(LocalDateTime.now().plusHours(24))
                .activa(true)
                .build();
        jpaRepository.save(sesion);
    }

    @Override
    @Transactional
    public void actualizarSesionMetadata(String token, String refreshToken,
            String ipOrigen, String userAgent) {
        jpaRepository.actualizarMetadataPorToken(token, refreshToken, ipOrigen, userAgent);
    }

    @Override
    @Transactional(readOnly = true)
    public String obtenerRefreshTokenPorToken(String token) {
        return jpaRepository.findByToken(token)
                .map(SesionEntity::getRefreshToken)
                .orElse(null);
    }

    @Override
    @Transactional
    public boolean invalidarSesion(String token) {
        return jpaRepository.invalidarPorToken(token) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean esSesionValida(String token) {
        return jpaRepository.existeSesionActivaYVigente(token, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public Long obtenerIdUsuarioPorToken(String token) {
        return jpaRepository.findByToken(token)
                .filter(s -> s.isActiva() && s.getFechaExpiracion().isAfter(LocalDateTime.now()))
                .map(SesionEntity::getIdUsuario)
                .orElse(null);
    }
}
