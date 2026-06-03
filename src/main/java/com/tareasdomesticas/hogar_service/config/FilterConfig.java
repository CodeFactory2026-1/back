package com.tareasdomesticas.hogar_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tareasdomesticas.hogar_service.auth.domain.port.out.SesionRepository;
import com.tareasdomesticas.hogar_service.infrastructure.filter.TokenAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    /**
     * Registra TokenAuthFilter para todos los endpoints /api/**
     * con orden 1 (antes de cualquier otro filtro de la app).
     */
    @Bean
    public FilterRegistrationBean<TokenAuthFilter> tokenAuthFilter(
            SesionRepository sesionRepository,
            ObjectMapper objectMapper) {

        FilterRegistrationBean<TokenAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TokenAuthFilter(sesionRepository, objectMapper));
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1);
        registration.setName("tokenAuthFilter");
        return registration;
    }
}
