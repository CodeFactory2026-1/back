package com.tareasdomesticas.hogar_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tareasdomesticas.hogar_service.hogares.application.service.CrearHogarService;
import com.tareasdomesticas.hogar_service.hogares.application.port.in.CrearHogarUseCase;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.hogares.infrastructure.adapter.out.InMemoryHogarRepository;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.AsignarTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.CrearTareaUseCase;
import com.tareasdomesticas.hogar_service.tareas.application.service.AsignarTareaService;
import com.tareasdomesticas.hogar_service.tareas.application.service.CrearTareaService;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.TareaRepository;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.InMemoryTareaRepository;

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
    @Bean
    public TareaRepository tareaRepository() {
        return new InMemoryTareaRepository();
    }

    @Bean
    public CrearTareaUseCase crearTareaUseCase(TareaRepository repo) {
        return new CrearTareaService(repo);
    }
    @Bean
    public AsignarTareaUseCase asignarTareaUseCase(TareaRepository tareaRepo, HogarRepository hogarRepo) {
        return new AsignarTareaService(tareaRepo, hogarRepo);
    }
}