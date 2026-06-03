package com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.repository;

import com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.entity.SesionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SesionJpaRepository extends JpaRepository<SesionEntity, Long> {

    Optional<SesionEntity> findByToken(String token);

    @Modifying
    @Query("UPDATE SesionEntity s SET s.activa = false WHERE s.token = :token AND s.activa = true")
    int invalidarPorToken(@Param("token") String token);

    @Modifying
    @Query("UPDATE SesionEntity s SET s.refreshToken = :refreshToken, s.ipOrigen = :ipOrigen, s.userAgent = :userAgent WHERE s.token = :token")
    int actualizarMetadataPorToken(@Param("token") String token,
            @Param("refreshToken") String refreshToken,
            @Param("ipOrigen") String ipOrigen,
            @Param("userAgent") String userAgent);

    @Modifying
    @Query("UPDATE SesionEntity s SET s.ipOrigen = :ipOrigen, s.userAgent = :userAgent WHERE s.token = :token")
    int actualizarIpUaPorToken(@Param("token") String token,
            @Param("ipOrigen") String ipOrigen,
            @Param("userAgent") String userAgent);

    /**
     * Retorna true si existe una sesión con ese token que esté activa
     * y cuya fecha de expiración sea posterior al momento actual.
     */
    @Query("SELECT COUNT(s) > 0 FROM SesionEntity s " +
           "WHERE s.token = :token AND s.activa = true AND s.fechaExpiracion > :ahora")
    boolean existeSesionActivaYVigente(@Param("token") String token,
                                        @Param("ahora") LocalDateTime ahora);
}
