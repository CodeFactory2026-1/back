package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AsignacionSemanalTareaId implements Serializable {

    @Column(name = "id_asignacion")
    private Long idAsignacion;

    @Column(name = "id_tarea")
    private Long idTarea;
}
