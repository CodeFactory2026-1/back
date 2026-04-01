package com.tareasdomesticas.hogar_service.application.service;

import com.tareasdomesticas.hogar_service.domain.model.Hogar;
import com.tareasdomesticas.hogar_service.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.domain.port.in.CrearHogarUseCase;
import com.tareasdomesticas.hogar_service.domain.port.out.HogarRepository;

public class CrearHogarService implements CrearHogarUseCase {
    private final HogarRepository hogarRepository;

    public CrearHogarService(HogarRepository hogarRepository) {
        this.hogarRepository = hogarRepository;
    }

    @Override
    public Hogar crearHogar(String nombre, String descripcion, Usuario usuario) {

        if (hogarRepository.buscarPorUsuarioId(usuario.getIdUsuario()).isPresent()) {
            throw new IllegalStateException("Ya hace parte de un hogar");
        }
        try {
            Hogar hogar = new Hogar(
                    generarId(),
                    nombre,
                    descripcion,
                    usuario);

            return hogarRepository.guardar(hogar);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    private Integer generarId() {
        return (int) (Math.random() * 10000);
    }
}
