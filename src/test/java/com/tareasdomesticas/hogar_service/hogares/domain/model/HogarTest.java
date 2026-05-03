package com.tareasdomesticas.hogar_service.hogares.domain.model;

import com.tareasdomesticas.hogar_service.common.domain.model.Usuario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HogarTest {

    private Usuario crearUsuario(Long id, String nombre, String correo) {
        return new Usuario(id, nombre, correo);
    }

    private Hogar crearHogarValido() {
        Usuario admin = crearUsuario(1L, "Admin", "admin@test.com");
        return new Hogar(1L, "Hogar Test", "descripcion", admin);
    }

    @Test
    void debeCrearHogarCorrectamente() {
        Hogar hogar = crearHogarValido();

        assertEquals("Hogar Test", hogar.getNombreHogar());
        assertEquals(1L, hogar.getAdministrador().getIdUsuario());
        assertEquals(1, hogar.getUsuarios().size());
    }

    @Test
    void debeFallarSiNombreEsNulo() {
        Usuario admin = crearUsuario(1L, "Admin", "admin@test.com");

        assertThrows(IllegalArgumentException.class, () ->
                new Hogar(1L, null, "desc", admin)
        );
    }

    @Test
    void debeFallarSiCreadorEsNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Hogar(1L, "Hogar", "desc", null)
        );
    }

    @Test
    void debeFallarSiNombreEsMuyCorto() {
        Usuario admin = crearUsuario(1L, "Admin", "admin@test.com");

        assertThrows(IllegalArgumentException.class, () ->
                new Hogar(1L, "AB", "desc", admin)
        );
    }
    @Test
    void debeAgregarMiembroCorrectamente() {
        Hogar hogar = crearHogarValido();

        Usuario miembro = crearUsuario(2L, "Maria", "maria@test.com");

        hogar.agregarMiembro(miembro);

        assertEquals(2, hogar.getUsuarios().size());
    }

    @Test
    void noDebeAgregarMiembroNull() {
        Hogar hogar = crearHogarValido();

        assertThrows(IllegalArgumentException.class, () ->
                hogar.agregarMiembro(null)
        );
    }

    @Test
    void noDebeAgregarCorreoDuplicado() {
        Hogar hogar = crearHogarValido();

        Usuario miembro = crearUsuario(2L, "Maria", "admin@test.com");

        assertThrows(IllegalStateException.class, () ->
                hogar.agregarMiembro(miembro)
        );
    }

    @Test
    void debeFallarSiCorreoEsVacio() {
        Hogar hogar = crearHogarValido();

        Usuario miembro = crearUsuario(2L, "Maria", "");

        assertThrows(IllegalArgumentException.class, () ->
                hogar.agregarMiembro(miembro)
        );
    }

    @Test
    void debeFallarSiCorreoNoEsValido() {
        Hogar hogar = crearHogarValido();

        Usuario miembro = crearUsuario(2L, "Maria", "correo-invalido");

        assertThrows(IllegalArgumentException.class, () ->
                hogar.agregarMiembro(miembro)
        );
    }

    @Test
    void debeEliminarMiembroCorrectamente() {
        Hogar hogar = crearHogarValido();

        Usuario miembro = crearUsuario(2L, "Maria", "maria@test.com");
        hogar.agregarMiembro(miembro);

        hogar.eliminarMiembro(2L);

        assertEquals(1, hogar.getUsuarios().size());
    }

    @Test
    void noDebeEliminarAdministrador() {
        Hogar hogar = crearHogarValido();

        assertThrows(IllegalStateException.class, () ->
                hogar.eliminarMiembro(1L)
        );
    }

    @Test
    void debeFallarSiUsuarioNoExiste() {
        Hogar hogar = crearHogarValido();

        assertThrows(IllegalStateException.class, () ->
                hogar.eliminarMiembro(999L)
        );
    }

    @Test
    void debeFallarSiIdEsNull() {
        Hogar hogar = crearHogarValido();

        assertThrows(IllegalArgumentException.class, () ->
                hogar.eliminarMiembro(null)
        );
    }

    @Test
    void getIdsUsuariosDebeExcluirNulls() {
        Hogar hogar = crearHogarValido();

        List<Long> ids = hogar.getIdsUsuarios();

        assertTrue(ids.contains(1L));
    }

    @Test
    void listaUsuariosDebeSerInmutable() {
        Hogar hogar = crearHogarValido();

        assertThrows(UnsupportedOperationException.class, () ->
                hogar.getUsuarios().add(crearUsuario(2L, "X", "x@test.com"))
        );
    }
}