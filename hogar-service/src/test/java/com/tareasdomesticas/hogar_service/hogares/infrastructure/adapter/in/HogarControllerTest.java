package com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tareasdomesticas.hogar_service.hogares.application.dto.CrearHogarResultDTO;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HogarController.class)
class HogarControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private CrearHogarUseCase      crearHogarUseCase;
    @MockBean private AgregarMiembroUseCase  agregarMiembroUseCase;
    @MockBean private EliminarMiembroUseCase eliminarMiembroUseCase;

    private Map<String, Object> bodyCompleto() {
        Map<String, Object> body = new HashMap<>();
        body.put("usuarioId",     1L);
        body.put("nombreUsuario", "Laura");
        body.put("correoUsuario", "laura@test.com");
        body.put("nombreHogar",   "Hogar Laura");
        body.put("descripcion",   "desc");
        return body;
    }

    @Test
    void debeCrearHogarYRetornar201() throws Exception {
        CrearHogarResultDTO dto = new CrearHogarResultDTO(1L, "Hogar Laura", "desc", 1L);
        when(crearHogarUseCase.crearHogar(any())).thenReturn(dto);

        mockMvc.perform(post("/api/hogares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bodyCompleto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Hogar creado exitosamente"))
                .andExpect(jsonPath("$.nombreHogar").value("Hogar Laura"));
    }

    @Test
    void debeCrearHogarSinDescripcionYRetornar201() throws Exception {
        CrearHogarResultDTO dto = new CrearHogarResultDTO(2L, "Hogar Maria", null, 2L);
        when(crearHogarUseCase.crearHogar(any())).thenReturn(dto);

        Map<String, Object> body = new HashMap<>();
        body.put("usuarioId",     2L);
        body.put("nombreUsuario", "Maria");
        body.put("correoUsuario", "maria@test.com");
        body.put("nombreHogar",   "Hogar Maria");

        mockMvc.perform(post("/api/hogares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Hogar creado exitosamente"));
    }

    @Test
    void debeRetornar400SiNombreHogarEsVacio() throws Exception {
        Map<String, Object> body = bodyCompleto();
        body.put("nombreHogar", "");

        mockMvc.perform(post("/api/hogares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeRetornar400SiNombreHogarTieneMenosDe3Caracteres() throws Exception {
        Map<String, Object> body = bodyCompleto();
        body.put("nombreHogar", "AB");

        mockMvc.perform(post("/api/hogares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeRetornar400SiUsuarioIdEsNulo() throws Exception {

        Map<String, Object> body = bodyCompleto();
        body.remove("usuarioId");

        mockMvc.perform(post("/api/hogares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles[0]")
                        .value("usuarioId: El usuarioId es obligatorio"));
    }

    @Test
    void debeRetornar400SiNombreUsuarioEsVacio() throws Exception {
        Map<String, Object> body = bodyCompleto();
        body.put("nombreUsuario", "");

        mockMvc.perform(post("/api/hogares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeRetornar400SiCorreoEsVacio() throws Exception {
        Map<String, Object> body = bodyCompleto();
        body.put("correoUsuario", "");

        mockMvc.perform(post("/api/hogares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeRetornar409SiUsuarioYaTieneHogar() throws Exception {
        when(crearHogarUseCase.crearHogar(any()))
                .thenThrow(new IllegalStateException(
                        "Ya hace parte de un hogar, por lo que no puede crear otro hogar"));

        mockMvc.perform(post("/api/hogares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bodyCompleto())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje")
                        .value("Ya hace parte de un hogar, por lo que no puede crear otro hogar"));
    }
}
