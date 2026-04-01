package com.tareasdomesticas.hogar_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tareasdomesticas.hogar_service.application.service.CrearHogarService;
import com.tareasdomesticas.hogar_service.domain.port.in.CrearHogarUseCase;
import com.tareasdomesticas.hogar_service.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.infrastructure.adapter.out.InMemoryHogarRepository;

@Configuration
public class BeanConfig {
    @Bean
    public HogarRepository hogarRepository() {
        return new InMemoryHogarRepository();
    }

    @Bean
    public CrearHogarUseCase crearHogarUseCase(HogarRepository repo) {
        return new CrearHogarService(repo);
    }
}

