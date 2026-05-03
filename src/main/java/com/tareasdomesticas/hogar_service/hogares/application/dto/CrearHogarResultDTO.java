package com.tareasdomesticas.hogar_service.hogares.application.dto;

public record CrearHogarResultDTO (
    Long idHogar,
    String nombreHogar,
    String descripcionHogar,
    Long idAdministrador
) {}
