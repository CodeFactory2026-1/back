package com.tareasdomesticas.hogar_service.tareas.application.service;

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
class ListarTareasServiceTest {

    @Mock private TareaRepository tareaRepository;
    @Mock private AsignacionSemanalRepository asignacionRepository;

    @InjectMocks
    private ListarTareasService service;

    private static final Long HOGAR_ID = 1L;

    private Tarea tarea1;
    private Tarea tarea2;

    @BeforeEach
    void setUp() {
        tarea1 = new Tarea(1L, HOGAR_ID, null, "Barrer", null, null,
                LocalDateTime.now().plusDays(3), DificultadTarea.BAJA, PrioridadTarea.MEDIA);
        tarea2 = new Tarea(2L, HOGAR_ID, null, "Fregar", null, null,
                LocalDateTime.now().plusDays(4), DificultadTarea.ALTA, PrioridadTarea.ALTA);
    }

    @Test
    void debeListarTodasLasTareas() {
        when(tareaRepository.listar()).thenReturn(List.of(tarea1, tarea2));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(anyLong(), eq(HOGAR_ID)))
                .thenReturn(Optional.empty());

        List<TareaListadoDTO> result = service.listarTodas();

        assertThat(result).hasSize(2);
    }

    @Test
    void debeListarPorHogar() {
        when(tareaRepository.listarPorHogar(HOGAR_ID)).thenReturn(List.of(tarea1));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(1L, HOGAR_ID))
                .thenReturn(Optional.empty());

        List<TareaListadoDTO> result = service.listarPorHogar(HOGAR_ID);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Barrer");
    }

    @Test
    void debeRetornarEstadoPendienteSiNoTieneAsignacion() {
        when(tareaRepository.listarPorHogar(HOGAR_ID)).thenReturn(List.of(tarea1));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(1L, HOGAR_ID))
                .thenReturn(Optional.empty());

        List<TareaListadoDTO> result = service.listarPorHogar(HOGAR_ID);

        assertThat(result.get(0).getEstado()).isEqualTo("PENDIENTE");
        assertThat(result.get(0).getUsuarioAsignado().getIdUsuario()).isNull();
    }

    @Test
    void debeRetornarEstadoYUsuarioSiTieneAsignacion() {
        AsignacionSemanalTarea ast = new AsignacionSemanalTarea(10L, 1L, 5L);

        when(tareaRepository.listarPorHogar(HOGAR_ID)).thenReturn(List.of(tarea1));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(1L, HOGAR_ID))
                .thenReturn(Optional.of(ast));

        List<TareaListadoDTO> result = service.listarPorHogar(HOGAR_ID);

        assertThat(result.get(0).getEstado()).isEqualTo("ASIGNADO");
        assertThat(result.get(0).getUsuarioAsignado().getIdUsuario()).isEqualTo(5L);
    }

    @Test
    void debeRetornarListaVaciaSiNoHayTareas() {
        when(tareaRepository.listarPorHogar(HOGAR_ID)).thenReturn(List.of());

        List<TareaListadoDTO> result = service.listarPorHogar(HOGAR_ID);

        assertThat(result).isEmpty();
        verifyNoInteractions(asignacionRepository);
    }

    @Test
    void debeListarVariasTareasConDistintosEstados() {
        AsignacionSemanalTarea ast = new AsignacionSemanalTarea(10L, 2L, 3L);
        ast.cambiarEstado(EstadoTarea.EN_PROCESO);

        when(tareaRepository.listarPorHogar(HOGAR_ID)).thenReturn(List.of(tarea1, tarea2));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(1L, HOGAR_ID))
                .thenReturn(Optional.empty());
        when(asignacionRepository.buscarAsignacionActivaDeTarea(2L, HOGAR_ID))
                .thenReturn(Optional.of(ast));

        List<TareaListadoDTO> result = service.listarPorHogar(HOGAR_ID);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEstado()).isEqualTo("PENDIENTE");
        assertThat(result.get(1).getEstado()).isEqualTo("EN_PROCESO");
    }
}
