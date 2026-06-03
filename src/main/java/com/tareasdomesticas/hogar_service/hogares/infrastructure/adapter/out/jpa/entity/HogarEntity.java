package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hogares")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HogarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hogar")
    private Long idHogar;

    @Column(name = "nombre_hogar", nullable = false, length = 50)
    private String nombreHogar;

    @Column(name = "descripcion_hogar", length = 255)
    private String descripcionHogar;

    /**
     * MERGE en lugar de ALL, sin orphanRemoval.
     *
     * CascadeType.ALL + orphanRemoval provocaba que al llamar a
     * savedHogar.getUsuarios().clear() en guardar(), Hibernate programaba
     * DELETE de todos los usuarios (incluido el admin). Luego el re-add
     * del admin generaba un INSERT sobre una PK existente → excepción.
     *
     * Con MERGE Hibernate sólo actualiza (UPDATE) los usuarios que ya
     * existen; la gestión del ciclo de vida de un usuario (alta/baja
     * del hogar) se hace explícitamente desde UsuarioJpaRepository.
     */
    @OneToMany(mappedBy = "hogar", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Builder.Default
    private List<UsuarioEntity> usuarios = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private java.time.LocalDateTime updatedAt;
}
