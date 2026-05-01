package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity;

import com.tareasdomesticas.hogar_service.tareas.domain.model.DificultadTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.PrioridadTarea;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tareas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Long idTarea;

    @Column(name = "id_hogar", nullable = false)
    private Long idHogar;

    @Column(name = "nombre_tarea", nullable = false, length = 30)
    private String nombreTarea;

    @Column(name = "descripcion_tarea", length = 200)
    private String descripcionTarea;

    @Column(name = "foto_tarea", length = 255)
    private String fotoTarea;

    @Column(name = "fecha_limite", nullable = false)
    private LocalDateTime fechaLimite;
    
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "dificultad_tarea", nullable = false, columnDefinition = "dificultad_tarea_enum")
    private DificultadTarea dificultadTarea;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad_tarea", nullable = false, columnDefinition = "prioridad_tarea_enum")
    private PrioridadTarea prioridadTarea;
}
