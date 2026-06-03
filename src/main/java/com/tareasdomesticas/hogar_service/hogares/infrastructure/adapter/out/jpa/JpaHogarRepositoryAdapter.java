package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa;

import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.entity.HogarEntity;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.entity.UsuarioEntity;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.mapper.HogarMapper;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.repository.HogarJpaRepository;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.repository.UsuarioJpaRepository;
import jakarta.persistence.EntityManager;
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

    private final HogarJpaRepository   jpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final EntityManager        em;

    @Override
    @Transactional
    public Hogar guardar(Hogar hogar) {

        // ── NUEVO HOGAR ───────────────────────────────────────────────────────
        if (hogar.getIdHogar() == null) {

            // 1. Persistir solo el hogar para obtener su id generado
            HogarEntity hogarEntity = HogarEntity.builder()
                    .nombreHogar(hogar.getNombreHogar())
                    .descripcionHogar(hogar.getDescripcionHogar())
                    .build();
            HogarEntity hogarGuardado = jpaRepository.saveAndFlush(hogarEntity);

            // 2. Actualizar cada usuario existente: asignarle hogar + rol
            hogar.getUsuarios().forEach(u -> {
                UsuarioEntity ue = usuarioJpaRepository.findById(u.getIdUsuario())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Usuario no encontrado: id=" + u.getIdUsuario()));
                ue.setHogar(hogarGuardado);
                ue.setRolUsuario(u.getRolUsuario());
                usuarioJpaRepository.saveAndFlush(ue);
            });

            // 3. Evitar recargar desde JPA (la lista usuarios aún puede estar
            //    desincronizada en el contexto de persistencia).
            //    Construir el dominio directamente desde los datos en memoria:
            //    asignar el id generado al objeto de dominio y devolverlo.
            em.refresh(hogarGuardado);   // sincroniza el estado de la entidad con la BD
            return HogarMapper.toDomain(hogarGuardado);
        }

        // ── HOGAR EXISTENTE (agregar / eliminar miembro) ──────────────────────
        HogarEntity hogarEntity = jpaRepository.findById(hogar.getIdHogar())
                .orElseThrow(() -> new IllegalArgumentException("Hogar no encontrado."));

        hogarEntity.setNombreHogar(hogar.getNombreHogar());
        hogarEntity.setDescripcionHogar(hogar.getDescripcionHogar());

        // Actualizar usuarios que siguen en el hogar
        hogar.getUsuarios().stream()
                .filter(u -> u.getIdUsuario() != null)
                .forEach(u -> {
                    UsuarioEntity ue = usuarioJpaRepository.findById(u.getIdUsuario())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Usuario no encontrado: id=" + u.getIdUsuario()));
                    ue.setHogar(hogarEntity);
                    ue.setRolUsuario(u.getRolUsuario());
                    usuarioJpaRepository.saveAndFlush(ue);
                });

        // Desasociar usuarios que ya no están en el dominio
        hogarEntity.getUsuarios().forEach(ue -> {
            boolean sigueEnHogar = hogar.getUsuarios().stream()
                    .anyMatch(u -> ue.getIdUsuario().equals(u.getIdUsuario()));
            if (!sigueEnHogar) {
                ue.setHogar(null);
                ue.setRolUsuario(
                        com.tareasdomesticas.hogar_service.common.domain.model.RolUsuario.MIEMBRO);
                usuarioJpaRepository.saveAndFlush(ue);
            }
        });

        jpaRepository.saveAndFlush(hogarEntity);
        em.refresh(hogarEntity);
        return HogarMapper.toDomain(hogarEntity);
    }

    @Override
    public Optional<Hogar> buscarPorId(Long hogarId) {
        return jpaRepository.findById(hogarId).map(HogarMapper::toDomain);
    }

    @Override
    public Optional<Hogar> buscarPorUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuariosIdUsuario(usuarioId).map(HogarMapper::toDomain);
    }

    @Override
    public Optional<Hogar> buscarPorCorreoUsuario(String correo) {
        return jpaRepository.findByUsuariosCorreoUsuario(correo).map(HogarMapper::toDomain);
    }
}
