package com.tareasdomesticas.hogar_service.infrastructure.adapter.in;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tareasdomesticas.hogar_service.domain.model.Usuario;
import com.tareasdomesticas.hogar_service.domain.port.in.CrearHogarUseCase;
import com.tareasdomesticas.hogar_service.infrastructure.adapter.in.DTO.CrearHogarRequest;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/hogares")
public class HogarController {
    private final CrearHogarUseCase crearHogarUseCase;

    public HogarController(CrearHogarUseCase crearHogarUseCase) {
        this.crearHogarUseCase = crearHogarUseCase;
    }

    @PostMapping
    public String crearHogar(@RequestBody CrearHogarRequest request) {
        System.out.println(request.getNombreHogar());
        Usuario usuario = new Usuario(
                request.getUsuarioId(),
                request.getNombreUsuario(),
                request.getCorreoUsuario());

        crearHogarUseCase.crearHogar(
                request.getNombreHogar(),
                request.getDescripcion(),
                usuario);

        return "Hogar creado exitosamente";
    }

}
