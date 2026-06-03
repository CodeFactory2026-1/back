package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asignaciones_semanales", uniqueConstraints = @UniqueConstraint(name = "uq_hogar_semana", columnNames = {
        "id_hogar", "fecha_asignacion" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionSemanalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Long idAsignacion;

    @Column(name = "id_hogar", nullable = false)
    private Long idHogar;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @OneToMany(mappedBy = "asignacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AsignacionSemanalTareaEntity> tareas = new ArrayList<>();

    // Nota: la tabla `asignaciones_semanales` del DDL no tiene columnas
    // `created_at`/`updated_at` (es inmutable después de insert), por lo
    // que no se mapean aquí.
}