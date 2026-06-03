package com.tareasdomesticas.hogar_service.auth.infrastructure.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import com.tareasdomesticas.hogar_service.common.domain.model.RolUsuario;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(name = "uq_usuarios_correo", columnNames = "correo_usuario"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CuentaUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre_usuario", nullable = false, length = 50)
    private String nombre;

    @Column(name = "correo_usuario", nullable = false, unique = true, columnDefinition = "citext")
    private String correo;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String hashContrasena;

    /**
     * NAMED_ENUM le dice a Hibernate 6 que envíe el valor como el tipo nativo
     * de Postgres (rol_usuario_enum) en lugar de VARCHAR, evitando el error
     * "column is of type rol_usuario_enum but expression is of type character varying".
     */
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "rol_usuario", nullable = false, columnDefinition = "rol_usuario_enum")
    private RolUsuario rolUsuario;

    @Column(name = "id_hogar")
    private Long idHogar;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private java.time.LocalDateTime updatedAt;
}
