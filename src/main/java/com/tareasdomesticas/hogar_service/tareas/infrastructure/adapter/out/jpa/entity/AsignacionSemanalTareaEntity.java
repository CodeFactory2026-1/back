package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity;

import com.tareasdomesticas.hogar_service.tareas.domain.model.EstadoTarea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "asignaciones_semanales_tareas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AsignacionSemanalTareaEntity {

    @EmbeddedId
    private AsignacionSemanalTareaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idAsignacion")
    @JoinColumn(name = "id_asignacion")
    private AsignacionSemanalEntity asignacion;

    @Column(name = "id_usuario_asignado")
    private Long idUsuarioAsignado;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado_tarea", nullable = false, columnDefinition = "estado_tarea_enum")
    @Builder.Default
    private EstadoTarea estadoTarea = EstadoTarea.PENDIENTE;

    @Column(name = "excedente", nullable = false)
    @Builder.Default
    private boolean excedente = false;

    @Column(name = "id_usuario_finalizador")
    private Long idUsuarioFinalizador;

    @Column(name = "fecha_ultimo_cambio_estado")
    private java.time.LocalDateTime fechaUltimoCambioEstado;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private java.time.LocalDateTime updatedAt;
}
