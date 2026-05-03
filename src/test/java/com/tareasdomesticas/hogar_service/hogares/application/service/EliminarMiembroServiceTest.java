package com.tareasdomesticas.hogar_service.hogares.application.service;

import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.EliminarMiembroCommand;
import com.tareasdomesticas.hogar_service.hogares.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.tareas.application.port.out.LiberarTareasPort;
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
class EliminarMiembroServiceTest {

    @Mock private HogarRepository   hogarRepository;
    @Mock private LiberarTareasPort liberarTareasPort;

    @InjectMocks
    private EliminarMiembroService service;

    private static final Long HOGAR_ID = 9999L;
    private static final Long ADMIN_ID = 1L;
    private static final Long MIEMBRO_ID = 2L;

    private Hogar hogar;

    @BeforeEach
    void setUp() {
        Usuario admin   = new Usuario(ADMIN_ID,   "Admin", "admin@correo.com");
        Usuario miembro = new Usuario(MIEMBRO_ID, "María", "maria@correo.com");
        hogar = new Hogar(HOGAR_ID, "Mi Hogar", null, admin);
        hogar.agregarMiembro(miembro);
    }

    @Test
    void debeEliminarMiembroYLiberarSusTareas() {
        when(hogarRepository.buscarPorId(HOGAR_ID)).thenReturn(Optional.of(hogar));
        when(hogarRepository.guardar(any())).thenReturn(hogar);

        service.eliminarMiembro(new EliminarMiembroCommand(HOGAR_ID, ADMIN_ID, MIEMBRO_ID));

        verify(hogarRepository).guardar(any());
        verify(liberarTareasPort).liberarTareasDeUsuario(MIEMBRO_ID, HOGAR_ID);
    }

    @Test
    void debeLiberarTareasConElHogarIdCorrecto() {
        when(hogarRepository.buscarPorId(HOGAR_ID)).thenReturn(Optional.of(hogar));
        when(hogarRepository.guardar(any())).thenReturn(hogar);

        service.eliminarMiembro(new EliminarMiembroCommand(HOGAR_ID, ADMIN_ID, MIEMBRO_ID));

        verify(liberarTareasPort, times(1)).liberarTareasDeUsuario(MIEMBRO_ID, HOGAR_ID);
    }

    @Test
    void debeLanzarExcepcionSiIntentaEliminarAdministrador() {
        when(hogarRepository.buscarPorId(HOGAR_ID)).thenReturn(Optional.of(hogar));

        assertThrows(IllegalStateException.class,
                () -> service.eliminarMiembro(
                        new EliminarMiembroCommand(HOGAR_ID, ADMIN_ID, ADMIN_ID)));

        verify(liberarTareasPort, never()).liberarTareasDeUsuario(any(), any());
    }

    @Test
    void debeLanzarExcepcionSiNoEsAdministrador() {
        when(hogarRepository.buscarPorId(HOGAR_ID)).thenReturn(Optional.of(hogar));

        assertThrows(IllegalStateException.class,
                () -> service.eliminarMiembro(
                        new EliminarMiembroCommand(HOGAR_ID, 99L, MIEMBRO_ID)));

        verify(liberarTareasPort, never()).liberarTareasDeUsuario(any(), any());
    }

    @Test
    void debeLanzarExcepcionSiHogarNoExiste() {
        when(hogarRepository.buscarPorId(HOGAR_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.eliminarMiembro(
                        new EliminarMiembroCommand(HOGAR_ID, ADMIN_ID, MIEMBRO_ID)));

        verify(liberarTareasPort, never()).liberarTareasDeUsuario(any(), any());
    }
}
