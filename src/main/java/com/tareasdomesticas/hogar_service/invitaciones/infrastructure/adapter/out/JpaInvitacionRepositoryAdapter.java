package com.tareasdomesticas.hogar_service.invitaciones.infrastructure.adapter.out;

import com.tareasdomesticas.hogar_service.invitaciones.domain.model.EstadoInvitacion;
import com.tareasdomesticas.hogar_service.invitaciones.domain.model.Invitacion;
import com.tareasdomesticas.hogar_service.invitaciones.domain.port.out.InvitacionRepository;
import com.tareasdomesticas.hogar_service.invitaciones.infrastructure.adapter.out.jpa.mapper.InvitacionMapper;
import com.tareasdomesticas.hogar_service.invitaciones.infrastructure.adapter.out.jpa.repository.InvitacionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaInvitacionRepositoryAdapter implements InvitacionRepository {

    private final InvitacionJpaRepository jpaRepository;

    @Override
    @Transactional
    public Invitacion guardar(Invitacion inv) {
        return InvitacionMapper.toDomain(jpaRepository.save(InvitacionMapper.toEntity(inv)));
    }

    @Override
    public Optional<Invitacion> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(InvitacionMapper::toDomain);
    }

    @Override
    public Optional<Invitacion> buscarPendientePorCorreoYHogar(String correo, Long idHogar) {
        // Pasamos el enum directamente — ya no String
        return jpaRepository.findByCorreoInvitadoAndIdHogarAndEstado(
                        correo, idHogar, EstadoInvitacion.PENDIENTE)
                .map(InvitacionMapper::toDomain);
    }

    @Override
    public List<Invitacion> listarPendientesPorCorreo(String correoInvitado) {
        return jpaRepository.findByCorreoInvitadoAndEstado(
                        correoInvitado, EstadoInvitacion.PENDIENTE)
                .stream().map(InvitacionMapper::toDomain).toList();
    }

    @Override
    public boolean existeMiembroConCorreo(String correo, Long idHogar) {
        return jpaRepository.existsMiembroConCorreo(correo, idHogar);
    }
}
