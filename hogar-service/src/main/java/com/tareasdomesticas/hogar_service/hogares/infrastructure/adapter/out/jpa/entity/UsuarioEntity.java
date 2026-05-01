package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.tareasdomesticas.hogar_service.common.domain.model.RolUsuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario", nullable = false, length = 50)
    private String nombreUsuario;

    @Column(name = "correo_usuario", nullable = false, unique = true, length = 254)
    private String correoUsuario;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "rol_usuario", nullable = false)
    private RolUsuario rolUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hogar", nullable = false)
    private HogarEntity hogar;
}