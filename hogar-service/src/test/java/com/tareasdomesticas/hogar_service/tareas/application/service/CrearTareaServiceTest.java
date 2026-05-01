package com.tareasdomesticas.hogar_service.tareas.application.service;

import com.tareasdomesticas.hogar_service.tareas.application.dto.CrearTareaResultDTO;
import com.tareasdomesticas.hogar_service.tareas.domain.model.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearTareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private CrearTareaService service;

    private final Long HOGAR_ID = 1L;
    private LocalDateTime fechaFutura;

    @BeforeEach
    void setUp() {
        fechaFutura = LocalDateTime.now().plusDays(5);
    }

    @Test
    void debeCrearTareaCorrectamente() {

        Tarea guardada = new Tarea(
                10L,
                HOGAR_ID,
                "Barrer",
                "Desc",
                null,
                fechaFutura,
                DificultadTarea.BAJA,
                PrioridadTarea.MEDIA
        );

        when(tareaRepository.existeTareaConMismoNombreEnSemana(any(), any(), any()))
                .thenReturn(false);

        when(tareaRepository.guardar(any()))
                .thenReturn(guardada);

        CrearTareaResultDTO resp = service.crearTarea(
                HOGAR_ID,
                "Barrer",
                "Desc",
                null,
                fechaFutura,
                "BAJA",
                "MEDIA"
        );

        assertThat(resp.getNombreTarea()).isEqualTo("Barrer");
        assertThat(resp.getDificultad()).isEqualTo("BAJA");
        assertThat(resp.getPrioridad()).isEqualTo("MEDIA");
        assertThat(resp.getEstado()).isEqualTo("PENDIENTE");

        verify(tareaRepository).guardar(any());
    }
    @Test
    void debeFallarSiYaExisteTareaEnLaSemana() {

        when(tareaRepository.existeTareaConMismoNombreEnSemana(any(), any(), any()))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.crearTarea(
                        HOGAR_ID,
                        "Barrer",
                        "Desc",
                        null,
                        fechaFutura,
                        "BAJA",
                        "MEDIA"
                )
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Ya existe una tarea");

        verify(tareaRepository, never()).guardar(any());
    }

    @Test
    void debeFallarSiDificultadEsInvalida() {

        assertThatThrownBy(() ->
                service.crearTarea(
                        HOGAR_ID,
                        "Barrer",
                        "Desc",
                        null,
                        fechaFutura,
                        "INVALIDA",
                        "MEDIA"
                )
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Dificultad inválida");

        verifyNoInteractions(tareaRepository);
    }

    @Test
    void debeFallarSiPrioridadEsInvalida() {

        assertThatThrownBy(() ->
                service.crearTarea(
                        HOGAR_ID,
                        "Barrer",
                        "Desc",
                        null,
                        fechaFutura,
                        "BAJA",
                        "INVALIDA"
                )
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Prioridad inválida");

        verifyNoInteractions(tareaRepository);
    }

    @Test
    void debeFallarSiDificultadEsNula() {

        assertThatThrownBy(() ->
                service.crearTarea(
                        HOGAR_ID,
                        "Barrer",
                        "Desc",
                        null,
                        fechaFutura,
                        null,
                        "MEDIA"
                )
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("dificultad es obligatoria");
    }
    @Test
    void debeFallarSiPrioridadEsNula() {

        assertThatThrownBy(() ->
                service.crearTarea(
                        HOGAR_ID,
                        "Barrer",
                        "Desc",
                        null,
                        fechaFutura,
                        "BAJA",
                        null
                )
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("prioridad es obligatoria");
    }

    @Test
    void debeFallarSiFechaEsPasada() {

        assertThatThrownBy(() ->
                service.crearTarea(
                        HOGAR_ID,
                        "Barrer",
                        "Desc",
                        null,
                        LocalDateTime.now().minusDays(1),
                        "BAJA",
                        "MEDIA"
                )
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("fecha límite");
    }

    @Test
    void debeFallarSiNombreEsInvalido() {

        assertThatThrownBy(() ->
                service.crearTarea(
                        HOGAR_ID,
                        "",
                        "Desc",
                        null,
                        fechaFutura,
                        "BAJA",
                        "MEDIA"
                )
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("nombre");
    }
}