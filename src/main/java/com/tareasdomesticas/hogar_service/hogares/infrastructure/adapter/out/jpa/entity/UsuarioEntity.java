package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.entity;

import com.tareasdomesticas.hogar_service.common.domain.model.RolUsuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario", nullable = false, length = 50)
    private String nombreUsuario;

    @Column(name = "correo_usuario", nullable = false, unique = true, columnDefinition = "citext")
    private String correoUsuario;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "rol_usuario", nullable = false, columnDefinition = "rol_usuario_enum")
    private RolUsuario rolUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hogar", nullable = true)
    private HogarEntity hogar;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private java.time.LocalDateTime updatedAt;
}
