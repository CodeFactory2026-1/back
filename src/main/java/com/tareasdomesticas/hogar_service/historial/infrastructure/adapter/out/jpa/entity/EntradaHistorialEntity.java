package com.tareasdomesticas.hogar_service.historial.infrastructure.adapter.out.jpa.entity;

import com.tareasdomesticas.hogar_service.historial.domain.model.TipoAccion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "actividades")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EntradaHistorialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad")
    private Long id;

    @Column(name = "id_hogar", nullable = false)
    private Long idHogar;

    @Column(name = "id_tarea")
    private Long idTarea;

    @Column(name = "nombre_tarea", length = 30)
    private String nombreTarea;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "tipo_accion", nullable = false, columnDefinition = "tipo_accion_enum")
    private TipoAccion tipoAccion;

    @Column(name = "id_usuario")
    private Long idUsuarioActor;

    @Column(name = "usuario_nombre", length = 50)
    private String nombreUsuarioActor;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "detalle", length = 500)
    private String detalle;
}
