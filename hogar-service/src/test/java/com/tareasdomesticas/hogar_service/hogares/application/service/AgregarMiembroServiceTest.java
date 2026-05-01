package com.tareasdomesticas.hogar_service.hogares.application.service;

import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.hogares.application.dto.MiembroDTO;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.AgregarMiembroCommand;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgregarMiembroServiceTest {

    @Mock private HogarRepository hogarRepository;
    @InjectMocks private AgregarMiembroService service;

    private Hogar hogar;
    private Usuario admin;

    @BeforeEach
    void setUp() {
        admin = new Usuario(1L, "Admin", "admin@correo.com");
        hogar = new Hogar(9999l, "Mi Hogar", null, admin);
    }

    @Test
    void debeAgregarMiembroExitosamente() {
        when(hogarRepository.buscarPorId(9999L)).thenReturn(Optional.of(hogar));
        when(hogarRepository.guardar(any())).thenReturn(hogar);

        MiembroDTO r = service.agregarMiembro(
                new AgregarMiembroCommand(9999L, 1L, "María", "maria@correo.com"));

        assertEquals("María", r.nombre());
        assertEquals("maria@correo.com", r.correo());
        verify(hogarRepository).guardar(any());
    }

    @Test
    void debeLanzarExcepcionSiCorreoDuplicado() {
        hogar.agregarMiembro(new Usuario(2L, "María", "maria@correo.com"));
        when(hogarRepository.buscarPorId(9999L)).thenReturn(Optional.of(hogar));

        assertThrows(IllegalStateException.class,
                () -> service.agregarMiembro(
                        new AgregarMiembroCommand(9999L, 1L, "Otro", "maria@correo.com")));
        verify(hogarRepository, never()).guardar(any());
    }

    @Test
    void debeLanzarExcepcionSiNombreVacio() {
        when(hogarRepository.buscarPorId(9999L)).thenReturn(Optional.of(hogar));

        assertThrows(IllegalArgumentException.class,
                () -> service.agregarMiembro(
                        new AgregarMiembroCommand(9999L, 1L, "", "nuevo@correo.com")));
    }

    @Test
    void debeLanzarExcepcionSiCorreoInvalido() {
        when(hogarRepository.buscarPorId(9999L)).thenReturn(Optional.of(hogar));

        assertThrows(IllegalArgumentException.class,
                () -> service.agregarMiembro(
                        new AgregarMiembroCommand(9999L, 1L, "Nuevo", "correo-invalido")));
    }

    @Test
    void debeLanzarExcepcionSiNoEsAdministrador() {
        when(hogarRepository.buscarPorId(9999L)).thenReturn(Optional.of(hogar));

        assertThrows(IllegalStateException.class,
                () -> service.agregarMiembro(
                        new AgregarMiembroCommand(9999L, 99L, "Nuevo", "nuevo@correo.com")));
    }

    @Test
    void debeLanzarExcepcionSiHogarNoExiste() {
        when(hogarRepository.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.agregarMiembro(
                        new AgregarMiembroCommand(1L, 1L, "Nuevo", "nuevo@correo.com")));
    }
}
