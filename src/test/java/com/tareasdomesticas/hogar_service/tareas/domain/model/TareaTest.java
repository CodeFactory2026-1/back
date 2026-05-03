package com.tareasdomesticas.hogar_service.tareas.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TareaTest {

    private Tarea crearTareaValida() {
        return new Tarea(
                1L,
                1L,
                "Barrer",
                "Limpiar sala",
                "imagen.jpg",
                LocalDateTime.now().plusDays(3),
                DificultadTarea.BAJA,
                PrioridadTarea.MEDIA
        );
    }

    @Test
    void debeCrearTareaCorrectamente() {
        Tarea tarea = crearTareaValida();

        assertEquals("Barrer", tarea.getNombreTarea());
        assertEquals(1L, tarea.getIdHogar());
        assertEquals(DificultadTarea.BAJA, tarea.getDificultad());
        assertEquals(PrioridadTarea.MEDIA, tarea.getPrioridad());
    }

    @Test
    void debeFallarSiIdHogarEsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Tarea(
                        1L, null, "Barrer", null, null,
                        LocalDateTime.now().plusDays(1),
                        DificultadTarea.BAJA,
                        PrioridadTarea.MEDIA
                )
        );
    }

    @Test
    void debeFallarSiNombreEsNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Tarea(
                        1L, 1L, null, null, null,
                        LocalDateTime.now().plusDays(1),
                        DificultadTarea.BAJA,
                        PrioridadTarea.MEDIA
                )
        );
    }

    @Test
    void debeFallarSiNombreEsMuyCorto() {
        assertThrows(IllegalArgumentException.class, () ->
                new Tarea(
                        1L, 1L, "AB", null, null,
                        LocalDateTime.now().plusDays(1),
                        DificultadTarea.BAJA,
                        PrioridadTarea.MEDIA
                )
        );
    }

    @Test
    void debeFallarSiDescripcionMuyLarga() {
        String descripcion = "a".repeat(201);

        assertThrows(IllegalArgumentException.class, () ->
                new Tarea(
                        1L, 1L, "Barrer", descripcion, null,
                        LocalDateTime.now().plusDays(1),
                        DificultadTarea.BAJA,
                        PrioridadTarea.MEDIA
                )
        );
    }

    @Test
    void debeFallarSiFechaEsPasada() {
        assertThrows(IllegalArgumentException.class, () ->
                new Tarea(
                        1L, 1L, "Barrer", null, null,
                        LocalDateTime.now().minusDays(1),
                        DificultadTarea.BAJA,
                        PrioridadTarea.MEDIA
                )
        );
    }

    @Test
    void debeFallarSiDificultadEsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Tarea(
                        1L, 1L, "Barrer", null, null,
                        LocalDateTime.now().plusDays(1),
                        null,
                        PrioridadTarea.MEDIA
                )
        );
    }

    @Test
    void debeFallarSiPrioridadEsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Tarea(
                        1L, 1L, "Barrer", null, null,
                        LocalDateTime.now().plusDays(1),
                        DificultadTarea.BAJA,
                        null
                )
        );
    }

    @Test
    void debeFallarSiFormatoFotoNoEsJpg() {
        assertThrows(IllegalArgumentException.class, () ->
                new Tarea(
                        1L, 1L, "Barrer", null, "foto.png",
                        LocalDateTime.now().plusDays(1),
                        DificultadTarea.BAJA,
                        PrioridadTarea.MEDIA
                )
        );
    }

    @Test
    void debeEditarTareaCorrectamente() {
        Tarea tarea = crearTareaValida();

        tarea.editar(
                "Trapear",
                "Limpiar piso",
                DificultadTarea.MEDIA,
                LocalDateTime.now().plusDays(5)
        );

        assertEquals("Trapear", tarea.getNombreTarea());
        assertEquals(DificultadTarea.MEDIA, tarea.getDificultad());
    }

    @Test
    void debeFallarEditarConNombreInvalido() {
        Tarea tarea = crearTareaValida();

        assertThrows(IllegalArgumentException.class, () ->
                tarea.editar("", null, DificultadTarea.MEDIA, LocalDateTime.now().plusDays(1))
        );
    }

    @Test
    void debeFallarEditarConFechaPasada() {
        Tarea tarea = crearTareaValida();

        assertThrows(IllegalArgumentException.class, () ->
                tarea.editar("Nuevo", null, DificultadTarea.MEDIA, LocalDateTime.now().minusDays(1))
        );
    }
    @Test
    void debeObtenerSemanaCorrectamente() {
        Tarea tarea = crearTareaValida();

        int semana = tarea.getSemana();

        assertTrue(semana > 0);
    }
}