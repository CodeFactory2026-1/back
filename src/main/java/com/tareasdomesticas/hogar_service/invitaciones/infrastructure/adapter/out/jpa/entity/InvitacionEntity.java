package com.tareasdomesticas.hogar_service.invitaciones.infrastructure.adapter.out.jpa.entity;

import com.tareasdomesticas.hogar_service.invitaciones.domain.model.EstadoInvitacion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitaciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InvitacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invitacion")
    private Long id;

    @Column(name = "id_hogar", nullable = false)
    private Long idHogar;

    @Column(name = "nombre_invitado", nullable = false, length = 50)
    private String nombreInvitado;

    @Column(name = "correo_invitado", nullable = false, columnDefinition = "citext")
    private String correoInvitado;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado", nullable = false, columnDefinition = "estado_invitacion_enum")
    private EstadoInvitacion estado;

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
