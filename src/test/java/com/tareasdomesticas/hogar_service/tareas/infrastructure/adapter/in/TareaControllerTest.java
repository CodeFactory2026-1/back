package com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tareasdomesticas.hogar_service.tareas.application.dto.AsignacionSemanalResponse;
import com.tareasdomesticas.hogar_service.tareas.application.dto.CrearTareaResultDTO;
import com.tareasdomesticas.hogar_service.tareas.application.dto.TareaListadoDTO;
import com.tareasdomesticas.hogar_service.tareas.application.dto.UsuarioAsignadoDTO;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.AsignarTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.CambiarEstadoTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.CrearTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.EditarTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.EliminarTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.FiltrarTareasUseCase;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.ListarTareasUseCase;
import com.tareasdomesticas.hogar_service.tareas.domain.model.DificultadTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.EstadoTarea;
import com.tareasdomesticas.hogar_service.tareas.domain.model.PrioridadTarea;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TareaController.class)
class TareaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrearTareaUseCase crearTareaUseCase;

    @MockBean
    private EditarTareaUseCase editarTareaUseCase;

    @MockBean
    private EliminarTareaUseCase eliminarTareaUseCase;

    @MockBean
    private CambiarEstadoTareaUseCase cambiarEstadoTareaUseCase;

    @MockBean
    private AsignarTareaUseCase asignarTareaUseCase;

    @MockBean
    private ListarTareasUseCase listarTareasUseCase;

    @MockBean
    private FiltrarTareasUseCase filtrarTareasUseCase;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void debeCrearTareaYRetornar200() throws Exception {
        CrearTareaResultDTO resultado = new CrearTareaResultDTO(
                1L, "Barrer la casa", null,
                LocalDateTime.now().plusDays(7),
                "BAJA", "ALTA", "PENDIENTE");

        when(crearTareaUseCase.crearTarea(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(resultado);

        Map<String, Object> body = Map.of(
                "idHogar", 1,
                "nombre", "Barrer la casa",
                "fechaLimite", LocalDateTime.now().plusDays(7).toString(),
                "dificultad", "BAJA",
                "prioridad", "ALTA");

        mockMvc.perform(post("/api/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Tarea creada exitosamente"));
    }

    @Test
    void debeRetornar400SiNombreEsVacio() throws Exception {
        Map<String, Object> body = Map.of(
                "idHogar", 1,
                "nombre", "",
                "fechaLimite", LocalDateTime.now().plusDays(7).toString(),
                "dificultad", "BAJA",
                "prioridad", "ALTA");

        mockMvc.perform(post("/api/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeRetornar400SiNombreTieneMenosDe3Caracteres() throws Exception {
        Map<String, Object> body = Map.of(
                "idHogar", 1,
                "nombre", "AB",
                "fechaLimite", LocalDateTime.now().plusDays(7).toString(),
                "dificultad", "BAJA",
                "prioridad", "ALTA");

        mockMvc.perform(post("/api/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeRetornar400SiHogarIdEsNulo() throws Exception {
        Map<String, Object> body = Map.of(
                "nombre", "Barrer",
                "fechaLimite", LocalDateTime.now().plusDays(7).toString(),
                "dificultad", "BAJA",
                "prioridad", "ALTA");

        mockMvc.perform(post("/api/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeRetornar400SiFechaEsPasada() throws Exception {
        Map<String, Object> body = Map.of(
                "idHogar", 1,
                "nombre", "Barrer",
                "fechaLimite", LocalDateTime.now().minusDays(1).toString(),
                "dificultad", "BAJA",
                "prioridad", "ALTA");

        mockMvc.perform(post("/api/tareas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeListarTodasLasTareas() throws Exception {
        TareaListadoDTO dto = new TareaListadoDTO(
                1L, 1L, "Barrer", "Descripción de la tarea", DificultadTarea.BAJA.name(),
                PrioridadTarea.ALTA.name(), EstadoTarea.PENDIENTE.name(),
                UsuarioAsignadoDTO.sinAsignar(),
                LocalDateTime.now().plusDays(7));

        when(listarTareasUseCase.listarTodas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/tareas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Barrer"));
    }

    @Test
    void debeListarTareasPorHogar() throws Exception {
        TareaListadoDTO dto = new TareaListadoDTO(
                1L, 9999L, "Lavar", "Descripción de la tarea", DificultadTarea.MEDIA.name(),
                PrioridadTarea.MEDIA.name(), EstadoTarea.PENDIENTE.name(),
                UsuarioAsignadoDTO.sinAsignar(),
                LocalDateTime.now().plusDays(7));

        when(listarTareasUseCase.listarPorHogar(9999L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/tareas/hogares/9999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Lavar"))
                .andExpect(jsonPath("$[0].idHogar").value(9999));
    }

    @Test
    void debeAsignarTareasYRetornar200() throws Exception {
        AsignacionSemanalResponse respuesta = new AsignacionSemanalResponse(
                "Tareas asignadas correctamente", 9999L, List.of(), List.of());

        when(asignarTareaUseCase.asignarTareasSemanales(9999L)).thenReturn(respuesta);

        mockMvc.perform(post("/api/tareas/hogares/9999/asignacion-semanal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Tareas asignadas correctamente"));
    }

    @Test
    void debeRetornar400SiYaSeAsignoEstaSemana() throws Exception {
        when(asignarTareaUseCase.asignarTareasSemanales(9999L))
                .thenThrow(new IllegalStateException("Solo se puede asignar una vez por semana"));

        mockMvc.perform(post("/api/tareas/hogares/9999/asignacion-semanal"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Solo se puede asignar una vez por semana"));
    }
}