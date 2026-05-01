package com.tareasdomesticas.hogar_service.tareas.application.service;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EliminarTareaServiceTest {

    @Mock private TareaRepository tareaRepository;
    @Mock private AsignacionSemanalRepository asignacionRepository;

    @InjectMocks
    private EliminarTareaService service;

    private static final Long TAREA_ID = 1L;
    private static final Long HOGAR_ID = 9999L;

    private Tarea tarea;

    @BeforeEach
    void setUp() {
        tarea = new Tarea(
                TAREA_ID, HOGAR_ID, "Barrer", null, null,
                LocalDateTime.now().plusDays(3),
                DificultadTarea.BAJA, PrioridadTarea.MEDIA);
    }

    @Test
    void debeEliminarTareaPendienteExitosamente() {
        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.empty());

        service.eliminarTarea(TAREA_ID);

        verify(tareaRepository).eliminar(TAREA_ID);
    }

    @Test
    void debeFallarSiTareaNoExiste() {
        when(tareaRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.eliminarTarea(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no existe");

        verify(tareaRepository, never()).eliminar(any());
    }

    @Test
    void debeFallarSiTareaEstaAsignada() {
        AsignacionSemanalTarea ast = new AsignacionSemanalTarea(10L, TAREA_ID, 2L);

        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(ast));

        assertThatThrownBy(() -> service.eliminarTarea(TAREA_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("asignada o en progreso");

        verify(tareaRepository, never()).eliminar(any());
    }

    @Test
    void debeFallarSiTareaEstaEnProceso() {
        AsignacionSemanalTarea ast = new AsignacionSemanalTarea(10L, TAREA_ID, 2L);
        ast.cambiarEstado(EstadoTarea.EN_PROCESO);

        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(ast));

        assertThatThrownBy(() -> service.eliminarTarea(TAREA_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("asignada o en progreso");
    }

    @Test
    void debePermitirEliminarTareaExcedente() {
        // Una tarea excedente está en estado PENDIENTE, sí puede eliminarse
        AsignacionSemanalTarea excedente = new AsignacionSemanalTarea(10L, TAREA_ID);

        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(excedente));

        service.eliminarTarea(TAREA_ID);

        verify(tareaRepository).eliminar(TAREA_ID);
    }
}
