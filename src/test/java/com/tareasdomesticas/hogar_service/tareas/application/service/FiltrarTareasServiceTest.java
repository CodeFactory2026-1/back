package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.tareas.application.dto.FiltroTareasDTO;
import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.AsignacionSemanalRepository;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FiltrarTareasServiceTest {

    @Mock private TareaRepository tareaRepository;
    @Mock private AsignacionSemanalRepository asignacionRepository;

    @InjectMocks
    private FiltrarTareasService service;

    private static final Long HOGAR_ID = 1L;

    private Tarea tareaAlta;
    private Tarea tareaBaja;

    @BeforeEach
    void setUp() {
        tareaAlta = new Tarea(1L, HOGAR_ID, null, "Limpiar cocina", null, null,
                LocalDateTime.now().plusDays(3), DificultadTarea.ALTA, PrioridadTarea.ALTA);
        tareaBaja = new Tarea(2L, HOGAR_ID, null, "Barrer sala", null, null,
                LocalDateTime.now().plusDays(3), DificultadTarea.BAJA, PrioridadTarea.BAJA);
    }

    @Test
    void debeRetornarTodasSinFiltros() {
        when(tareaRepository.filtrar(HOGAR_ID, null, null, null))
                .thenReturn(List.of(tareaAlta, tareaBaja));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(anyLong(), eq(HOGAR_ID)))
                .thenReturn(Optional.empty());

        List<TareaListadoDTO> result = service.filtrar(
                new FiltroTareasDTO(HOGAR_ID, null, null, null, null, null));

        assertThat(result).hasSize(2);
    }

    @Test
    void debeRetornarSoloPendientesSinAsignacion() {
        when(tareaRepository.filtrar(eq(HOGAR_ID), any(), any(), any()))
                .thenReturn(List.of(tareaAlta, tareaBaja));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(anyLong(), eq(HOGAR_ID)))
                .thenReturn(Optional.empty());

        List<TareaListadoDTO> result = service.filtrar(
                new FiltroTareasDTO(HOGAR_ID, "PENDIENTE", null, null, null, null));

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(t -> t.getEstado().equals("PENDIENTE"));
    }

    @Test
    void debeRetornarSoloTareasAsignadas() {
        AsignacionSemanalTarea ast = new AsignacionSemanalTarea(10L, 1L, 5L);

        when(tareaRepository.filtrar(eq(HOGAR_ID), any(), any(), any()))
                .thenReturn(List.of(tareaAlta, tareaBaja));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(1L, HOGAR_ID))
                .thenReturn(Optional.of(ast));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(2L, HOGAR_ID))
                .thenReturn(Optional.empty());

        List<TareaListadoDTO> result = service.filtrar(
                new FiltroTareasDTO(HOGAR_ID, "ASIGNADO", null, null, null, null));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEstado()).isEqualTo("ASIGNADO");
    }

    @Test
    void debeRetornarListaVaciaSiNoCumpleFiltros() {
        when(tareaRepository.filtrar(eq(HOGAR_ID), any(), any(), any()))
                .thenReturn(List.of(tareaAlta));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(1L, HOGAR_ID))
                .thenReturn(Optional.empty());

        List<TareaListadoDTO> result = service.filtrar(
                new FiltroTareasDTO(HOGAR_ID, "FINALIZADO", null, null, null, null));

        assertThat(result).isEmpty();
    }

    @Test
    void debeFallarSiEstadoEsInvalido() {
        assertThatThrownBy(() -> service.filtrar(
                new FiltroTareasDTO(HOGAR_ID, "ESTADO_RARO", null, null, null, null))
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Valor no válido");
    }

    @Test
    void debeFiltrarPorUsuarioAsignado() {
        AsignacionSemanalTarea astUser5 = new AsignacionSemanalTarea(10L, 1L, 5L);
        AsignacionSemanalTarea astUser9 = new AsignacionSemanalTarea(10L, 2L, 9L);

        when(tareaRepository.filtrar(eq(HOGAR_ID), any(), any(), any()))
                .thenReturn(List.of(tareaAlta, tareaBaja));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(1L, HOGAR_ID))
                .thenReturn(Optional.of(astUser5));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(2L, HOGAR_ID))
                .thenReturn(Optional.of(astUser9));

        List<TareaListadoDTO> result = service.filtrar(
                new FiltroTareasDTO(HOGAR_ID, null, 5L, null, null, null));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsuarioAsignado().getIdUsuario()).isEqualTo(5L);
    }
}
