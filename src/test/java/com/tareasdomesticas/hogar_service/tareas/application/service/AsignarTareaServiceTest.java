package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.tareas.application.dto.AsignacionSemanalResponse;
import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaAsignadaDTO;
import com.tareasdomesticas.hogar_service.tareas.application.port.out.ObtenerMiembrosHogarPort;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.AsignacionSemanalRepository;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsignarTareaServiceTest {

    @Mock private TareaRepository tareaRepository;
    @Mock private AsignacionSemanalRepository asignacionRepository;
    @Mock private ObtenerMiembrosHogarPort obtenerMiembrosPort;

    @InjectMocks
    private AsignarTareaService service;

    private final Long HOGAR_ID = 1L;

    private Tarea tarea(Long id, String nombre, DificultadTarea dif) {
        return new Tarea(
                id,
                HOGAR_ID,
                id, 
                nombre,
                null,
                null,
                LocalDateTime.now().plusDays(5),
                dif,
                PrioridadTarea.MEDIA
        );
    }

    private AsignacionSemanal asignacionMock() {
        return new AsignacionSemanal(10L, HOGAR_ID, LocalDate.now());
    }

    @BeforeEach
    void baseMocks() {
        when(asignacionRepository.obtenerUltimaAsignacion(HOGAR_ID))
                .thenReturn(Optional.empty());

        when(asignacionRepository.obtenerIdsTareasExcedentes(HOGAR_ID))
                .thenReturn(List.of());

        when(asignacionRepository.listarPorAsignacion(any()))
                .thenReturn(List.of());

        when(asignacionRepository.guardarAsignacion(any()))
                .thenReturn(asignacionMock());
    }

    @Test
    void debeAsignarTareasEquitativamente() {

        when(obtenerMiembrosPort.obtenerIdsUsuarios(HOGAR_ID))
                .thenReturn(List.of(1L, 2L));

        when(tareaRepository.listarPendientesPorHogar(eq(HOGAR_ID), anyList()))
                .thenReturn(List.of(
                        tarea(1L, "A", DificultadTarea.BAJA),
                        tarea(2L, "B", DificultadTarea.ALTA)
                ));

        AsignacionSemanalResponse resp =
                service.asignarTareasSemanales(HOGAR_ID);

        assertThat(resp.getTareasAsignadas()).hasSize(2);
        assertThat(resp.getTareasExcedentes()).isEmpty();

        verify(asignacionRepository, times(2))
                .guardarAsignacionTarea(any());
    }


    @Test
    void debeGenerarExcedentes() {

        when(obtenerMiembrosPort.obtenerIdsUsuarios(HOGAR_ID))
                .thenReturn(List.of(1L, 2L));

        when(tareaRepository.listarPendientesPorHogar(eq(HOGAR_ID), anyList()))
                .thenReturn(List.of(
                        tarea(1L, "A", DificultadTarea.BAJA),
                        tarea(2L, "B", DificultadTarea.BAJA),
                        tarea(3L, "C", DificultadTarea.BAJA)
                ));

        AsignacionSemanalResponse resp =
                service.asignarTareasSemanales(HOGAR_ID);

        assertThat(resp.getTareasAsignadas()).hasSize(2);
        assertThat(resp.getTareasExcedentes()).hasSize(1);
    }

    @Test
    void debeFallarSiYaSeAsignoEstaSemana() {

        AsignacionSemanal ultima =
                new AsignacionSemanal(1L, HOGAR_ID, LocalDate.now());

        when(asignacionRepository.obtenerUltimaAsignacion(HOGAR_ID))
                .thenReturn(Optional.of(ultima));

        assertThatThrownBy(() -> service.asignarTareasSemanales(HOGAR_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("una vez por semana");
    }

    @Test
    void debeFallarSiNoHayUsuarios() {

        when(obtenerMiembrosPort.obtenerIdsUsuarios(HOGAR_ID))
                .thenReturn(List.of());

        assertThatThrownBy(() -> service.asignarTareasSemanales(HOGAR_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No se encontró el hogar");
    }


    @Test
    void debeFallarSiNoHayTareas() {

        when(obtenerMiembrosPort.obtenerIdsUsuarios(HOGAR_ID))
                .thenReturn(List.of(1L, 2L));

        when(tareaRepository.listarPendientesPorHogar(eq(HOGAR_ID), anyList()))
                .thenReturn(List.of());

        assertThatThrownBy(() -> service.asignarTareasSemanales(HOGAR_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No hay tareas");
    }


    @Test
    void debeAsignarPorMenorCarga() {

        when(obtenerMiembrosPort.obtenerIdsUsuarios(HOGAR_ID))
                .thenReturn(List.of(1L, 2L));

        when(tareaRepository.listarPendientesPorHogar(eq(HOGAR_ID), anyList()))
                .thenReturn(List.of(
                        tarea(1L, "Alta1", DificultadTarea.ALTA),
                        tarea(2L, "Alta2", DificultadTarea.ALTA)
                ));

        AsignacionSemanalResponse resp =
                service.asignarTareasSemanales(HOGAR_ID);

        List<Long> usuarios = resp.getTareasAsignadas()
                .stream()
                .map(TareaAsignadaDTO::getUsuarioAsignado)
                .toList();

        assertThat(usuarios).doesNotHaveDuplicates();
    }

    @Test
    void debePriorizarExcedentes() {

        when(obtenerMiembrosPort.obtenerIdsUsuarios(HOGAR_ID))
                .thenReturn(List.of(1L));

        when(asignacionRepository.obtenerIdsTareasExcedentes(HOGAR_ID))
                .thenReturn(List.of(1L));

        when(tareaRepository.listarPendientesPorHogar(eq(HOGAR_ID), anyList()))
                .thenReturn(List.of(
                        tarea(1L, "Excedente", DificultadTarea.BAJA),
                        tarea(2L, "Nueva ALTA", DificultadTarea.ALTA)
                ));

        AsignacionSemanalResponse resp =
                service.asignarTareasSemanales(HOGAR_ID);

        assertThat(resp.getTareasAsignadas()).hasSize(1);
        assertThat(resp.getTareasAsignadas().get(0).getNombre())
                .isEqualTo("Excedente");
    }
}