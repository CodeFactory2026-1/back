package com.tareasdomesticas.hogar_service.hogares.application.service;

import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.hogares.application.dto.CrearHogarResultDTO;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.CrearHogarCommand;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearHogarServiceTest {

    @Mock
    private HogarRepository hogarRepository;

    private CrearHogarService service;

    @BeforeEach
    void setUp() {
        service = new CrearHogarService(hogarRepository);
    }

    @Test
    void debeCrearHogarYConvertirCreadorEnAdministrador() {
        Usuario usuario = new Usuario(null, "Laura", "laura@test.com");
        Hogar hogarEsperado = new Hogar(1L, "Hogar Laura", "desc", usuario);

        when(hogarRepository.buscarPorCorreoUsuario("laura@test.com"))
                .thenReturn(Optional.empty());
        when(hogarRepository.guardar(any())).thenReturn(hogarEsperado);

        CrearHogarResultDTO resultado = service.crearHogar(new CrearHogarCommand(
                null, "Laura", "laura@test.com", "Hogar Laura", "desc"));

        assertThat(resultado.nombreHogar()).isEqualTo("Hogar Laura");
        assertThat(resultado.idAdministrador()).isNotNull();
        verify(hogarRepository).guardar(any(Hogar.class));
    }

    @Test
    void debePermitirCrearHogarSinDescripcion() {
        Usuario usuario = new Usuario(null, "Maria", "maria@test.com");
        Hogar hogarEsperado = new Hogar(2L, "Hogar Maria", null, usuario);

        when(hogarRepository.buscarPorCorreoUsuario("maria@test.com"))
                .thenReturn(Optional.empty());
        when(hogarRepository.guardar(any())).thenReturn(hogarEsperado);

        CrearHogarResultDTO resultado = service.crearHogar(new CrearHogarCommand(
                null, "Maria", "maria@test.com", "Hogar Maria", null));

        assertThat(resultado.nombreHogar()).isEqualTo("Hogar Maria");
        assertThat(resultado.descripcionHogar()).isNull();
    }

    @Test
    void debeFallarSiUsuarioYaPerteneceAUnHogar() {
        Usuario usuario = new Usuario(null, "Pedro", "pedro@test.com");
        Hogar hogarExistente = new Hogar(1L, "Hogar Existente", null, usuario);

        when(hogarRepository.buscarPorCorreoUsuario("pedro@test.com"))
                .thenReturn(Optional.of(hogarExistente));

        assertThatThrownBy(() -> service.crearHogar(new CrearHogarCommand(
                null, "Pedro", "pedro@test.com", "Nuevo Hogar", null)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Ya hace parte de un hogar");

        verify(hogarRepository, never()).guardar(any());
    }

    @Test
    void debeFallarSiNombreEsNulo() {
        when(hogarRepository.buscarPorCorreoUsuario("ana@test.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.crearHogar(new CrearHogarCommand(
                null, "Ana", "ana@test.com", null, null)))
                .isInstanceOf(Exception.class);

        verify(hogarRepository, never()).guardar(any());
    }

    @Test
    void debeFallarSiNombreTieneMenosDe3Caracteres() {
        when(hogarRepository.buscarPorCorreoUsuario("luis@test.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.crearHogar(new CrearHogarCommand(
                null, "Luis", "luis@test.com", "AB", null)))
                .isInstanceOf(Exception.class);

        verify(hogarRepository, never()).guardar(any());
    }

    @Test
    void debeFallarSiNombreTieneMasDe50Caracteres() {
        when(hogarRepository.buscarPorCorreoUsuario("julia@test.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.crearHogar(new CrearHogarCommand(
                null, "Julia", "julia@test.com", "A".repeat(51), null)))
                .isInstanceOf(Exception.class);

        verify(hogarRepository, never()).guardar(any());
    }

    @Test
    void debeFallarSiNombreUsuarioEsNulo() {
        assertThatThrownBy(() -> service.crearHogar(new CrearHogarCommand(
                null, null, "sinid@test.com", "Hogar Test", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    void debeFallarSiCorreoEsNulo() {
        assertThatThrownBy(() -> service.crearHogar(new CrearHogarCommand(
                null, "Ana", null, "Hogar Test", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("correo");
    }
}
