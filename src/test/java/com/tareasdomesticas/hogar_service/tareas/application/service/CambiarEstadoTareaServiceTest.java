package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.CambiarEstadoCommand;
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
class CambiarEstadoTareaServiceTest {

    @Mock private TareaRepository tareaRepository;
    @Mock private AsignacionSemanalRepository asignacionRepository;

    @InjectMocks
    private CambiarEstadoTareaService service;

    private static final Long TAREA_ID     = 1L;
    private static final Long HOGAR_ID     = 9999L;
    private static final Long ASIGNACION_ID = 10L;
    private static final Long USUARIO_ID   = 2L;

    private Tarea tarea;
    private AsignacionSemanalTarea astAsignada;

    @BeforeEach
    void setUp() {
        tarea = new Tarea(
                TAREA_ID, HOGAR_ID, "Barrer", "Desc", null,
                LocalDateTime.now().plusDays(5),
                DificultadTarea.BAJA, PrioridadTarea.MEDIA);

        // Tarea asignada (estado ASIGNADO, tiene usuario)
        astAsignada = new AsignacionSemanalTarea(ASIGNACION_ID, TAREA_ID, USUARIO_ID);
    }

    @Test
    void debeCambiarDeAsignadoAEnProceso() {
        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(astAsignada));

        TareaListadoDTO resp = service.cambiarEstado(
                new CambiarEstadoCommand(TAREA_ID, "EN_PROCESO"));

        assertThat(resp.getEstado()).isEqualTo("EN_PROCESO");
        assertThat(resp.getUsuarioAsignado().getIdUsuario()).isEqualTo(USUARIO_ID);
        verify(asignacionRepository).actualizarAsignacionTarea(astAsignada);
    }

    @Test
    void debeCambiarDeEnProcesoAFinalizado() {
        AsignacionSemanalTarea enProceso = new AsignacionSemanalTarea(ASIGNACION_ID, TAREA_ID, USUARIO_ID);
        enProceso.cambiarEstado(EstadoTarea.EN_PROCESO);

        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(enProceso));

        TareaListadoDTO resp = service.cambiarEstado(
                new CambiarEstadoCommand(TAREA_ID, "FINALIZADO"));

        assertThat(resp.getEstado()).isEqualTo("FINALIZADO");
    }

    @Test
    void debeVolverAPendienteYQuitarUsuario() {
        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(astAsignada));

        TareaListadoDTO resp = service.cambiarEstado(
                new CambiarEstadoCommand(TAREA_ID, "PENDIENTE"));

        assertThat(resp.getEstado()).isEqualTo("PENDIENTE");
        assertThat(resp.getUsuarioAsignado().getIdUsuario()).isNull();
    }

    @Test
    void debeFallarSiTareaNoExiste() {
        when(tareaRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.cambiarEstado(new CambiarEstadoCommand(99L, "EN_PROCESO"))
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("no existe");

        verifyNoInteractions(asignacionRepository);
    }

    @Test
    void debeFallarSiNoTieneAsignacionActiva() {
        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.cambiarEstado(new CambiarEstadoCommand(TAREA_ID, "EN_PROCESO"))
        ).isInstanceOf(IllegalStateException.class)
         .hasMessageContaining("no tiene una asignación activa");

        verify(asignacionRepository, never()).actualizarAsignacionTarea(any());
    }

    @Test
    void debeFallarSiEstadoEsInvalido() {
        assertThatThrownBy(() ->
                service.cambiarEstado(new CambiarEstadoCommand(TAREA_ID, "ESTADO_FAKE"))
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Estado no válido");
    }

    @Test
    void debeFallarSiTareaEsExcedente() {
        // Una tarea excedente (sin usuario asignado) no puede cambiar estado
        AsignacionSemanalTarea excedente = new AsignacionSemanalTarea(ASIGNACION_ID, TAREA_ID);

        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(excedente));

        assertThatThrownBy(() ->
                service.cambiarEstado(new CambiarEstadoCommand(TAREA_ID, "EN_PROCESO"))
        ).isInstanceOf(IllegalStateException.class)
         .hasMessageContaining("debe ser asignada");
    }

    @Test
    void debeFallarSiTareaSinUsuarioIntentaCambiarEstado() {
        // Tarea liberada: excedente=false pero idUsuarioAsignado=null
        // (ocurre cuando se elimina el miembro responsable)
        AsignacionSemanalTarea sinUsuario = AsignacionSemanalTarea.reconstruir(
                ASIGNACION_ID, TAREA_ID, null, EstadoTarea.PENDIENTE, false);

        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(sinUsuario));

        assertThatThrownBy(() ->
                service.cambiarEstado(new CambiarEstadoCommand(TAREA_ID, "ASIGNADO"))
        ).isInstanceOf(IllegalStateException.class)
         .hasMessageContaining("no tiene un responsable asignado");

        verify(asignacionRepository, never()).actualizarAsignacionTarea(any());
    }

    @Test
    void debeFallarSiTransicionNoEsValida() {
        // ASIGNADO → FINALIZADO no está permitido, debe pasar por EN_PROCESO
        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(astAsignada));

        assertThatThrownBy(() ->
                service.cambiarEstado(new CambiarEstadoCommand(TAREA_ID, "FINALIZADO"))
        ).isInstanceOf(IllegalStateException.class)
         .hasMessageContaining("Transición no permitida");
    }

    @Test
    void debeFallarSiTareaEstaFinalizada() {
        AsignacionSemanalTarea finalizada = new AsignacionSemanalTarea(ASIGNACION_ID, TAREA_ID, USUARIO_ID);
        finalizada.cambiarEstado(EstadoTarea.EN_PROCESO);
        finalizada.cambiarEstado(EstadoTarea.FINALIZADO);

        when(tareaRepository.buscarPorId(TAREA_ID)).thenReturn(Optional.of(tarea));
        when(asignacionRepository.buscarAsignacionActivaDeTarea(TAREA_ID, HOGAR_ID))
                .thenReturn(Optional.of(finalizada));

        assertThatThrownBy(() ->
                service.cambiarEstado(new CambiarEstadoCommand(TAREA_ID, "PENDIENTE"))
        ).isInstanceOf(IllegalStateException.class)
         .hasMessageContaining("Transición no permitida");
    }
}