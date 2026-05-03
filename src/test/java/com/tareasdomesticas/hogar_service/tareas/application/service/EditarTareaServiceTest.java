package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.EditarTareaCommand;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.AsignacionSemanalRepository;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditarTareaServiceTest {

    @Mock private TareaRepository tareaRepository;
    @Mock private AsignacionSemanalRepository asignacionRepository;

    @InjectMocks
    private EditarTareaService service;

    private static final Long TAREA_ID = 1L;
    private static final Long HOGAR_ID = 9999L;

    private Tarea tareaOriginal;
    private LocalDateTime fechaFutura;

    @BeforeEach
    void setUp() {
        fechaFutura = LocalDateTime.now().plusDays(5);
        tareaOriginal = new Tarea(
                TAREA_ID, HOGAR_ID, "Barrer", "Descripcion original",
                null, fechaFutura, DificultadTarea.BAJA, PrioridadTarea.MEDIA);
    }

    @Test
    void debeEditarTareaExitosamente() {
        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tareaOriginal));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.empty());
        when(tareaRepository.actualizar(any())).thenReturn(tareaOriginal);

        TareaListadoDTO resp = service.editarTarea(new EditarTareaCommand(
                TAREA_ID, "Trapear", "Nueva desc", "MEDIA", fechaFutura));

        assertThat(resp.getNombre()).isEqualTo("Trapear");
        verify(tareaRepository).actualizar(any());
    }

    @Test
    void debeFallarSiTareaNoExiste() {
        when(tareaRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.editarTarea(
                new EditarTareaCommand(99L, "Nombre", null, "BAJA", fechaFutura))
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("no existe");

        verify(tareaRepository, never()).actualizar(any());
    }

    @Test
    void debeFallarSiTareaEstaAsignada() {
        AsignacionSemanalTarea ast = new AsignacionSemanalTarea(10L, TAREA_ID, 2L);

        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tareaOriginal));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(ast));

        assertThatThrownBy(() -> service.editarTarea(
                new EditarTareaCommand(TAREA_ID, "Nuevo", null, "ALTA", fechaFutura))
        ).isInstanceOf(IllegalStateException.class)
         .hasMessageContaining("asignada o en proceso");

        verify(tareaRepository, never()).actualizar(any());
    }

    @Test
    void debeFallarSiTareaEstaEnProceso() {
        AsignacionSemanalTarea ast = new AsignacionSemanalTarea(10L, TAREA_ID, 2L);
        ast.cambiarEstado(EstadoTarea.EN_PROCESO);

        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tareaOriginal));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(ast));

        assertThatThrownBy(() -> service.editarTarea(
                new EditarTareaCommand(TAREA_ID, "Nuevo", null, "ALTA", fechaFutura))
        ).isInstanceOf(IllegalStateException.class)
         .hasMessageContaining("asignada o en proceso");
    }

    @Test
    void debeFallarSiDificultadEsNula() {
        assertThatThrownBy(() -> service.editarTarea(
                new EditarTareaCommand(TAREA_ID, "Nombre", null, null, fechaFutura))
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("dificultad es obligatoria");
    }

    @Test
    void debeFallarSiDificultadEsInvalida() {
        assertThatThrownBy(() -> service.editarTarea(
                new EditarTareaCommand(TAREA_ID, "Nombre", null, "SUPERALTA", fechaFutura))
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Dificultad no válida");
    }

    @Test
    void debeFallarSiNombreEsInvalido() {
        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tareaOriginal));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.editarTarea(
                new EditarTareaCommand(TAREA_ID, "AB", null, "BAJA", fechaFutura))
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("caracteres");
    }

    @Test
    void debeFallarSiFechaEsPasada() {
        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tareaOriginal));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.editarTarea(
                new EditarTareaCommand(TAREA_ID, "Trapear", null,
                        "MEDIA", LocalDateTime.now().minusDays(1)))
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("fecha límite");
    }
}
