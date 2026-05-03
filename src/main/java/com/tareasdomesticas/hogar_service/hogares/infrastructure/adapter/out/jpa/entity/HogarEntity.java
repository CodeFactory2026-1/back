package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hogares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HogarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hogar")
    private Long idHogar;

    @Column(name = "nombre_hogar", nullable = false, length = 50)
    private String nombreHogar;

    @Column(name = "descripcion_hogar", length = 255)
    private String descripcionHogar;

    @OneToMany(mappedBy = "hogar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<UsuarioEntity> usuarios = new ArrayList<>();
}
