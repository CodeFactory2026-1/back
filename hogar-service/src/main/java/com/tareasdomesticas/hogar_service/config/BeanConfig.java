package com.tareasdomesticas.hogar_service.config;

import com.tareasdomesticas.hogar_service.hogares.application.port.in.*;
import com.tareasdomesticas.hogar_service.hogares.application.service.*;
import com.tareasdomesticas.hogar_service.hogares.domain.port.out.HogarRepository;
import com.tareasdomesticas.hogar_service.tareas.application.port.in.*;
import com.tareasdomesticas.hogar_service.tareas.application.port.out.*;
import com.tareasdomesticas.hogar_service.tareas.application.service.*;
import com.tareasdomesticas.hogar_service.tareas.domain.port.out.*;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.LiberarTareasAdapter;
import com.tareasdomesticas.hogar_service.tareas.infrastructure.adapter.out.ObtenerMiembrosHogarAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {
    @Bean
    public LiberarTareasPort liberarTareasPort(AsignacionSemanalRepository asignacionRepo) {
        return new LiberarTareasAdapter(asignacionRepo);
    }

    @Bean
    public ObtenerMiembrosHogarPort obtenerMiembrosHogarPort(HogarRepository hogarRepo) {
        return new ObtenerMiembrosHogarAdapter(hogarRepo);
    }

    @Bean
    public CrearHogarUseCase crearHogarUseCase(HogarRepository r) {
        return new CrearHogarService(r);
    }

    @Bean
    public AgregarMiembroUseCase agregarMiembroUseCase(HogarRepository r) {
        return new AgregarMiembroService(r);
    }

    @Bean
    public EliminarMiembroUseCase eliminarMiembroUseCase(
            HogarRepository h, LiberarTareasPort l) {
        return new EliminarMiembroService(h, l);
    }

    @Bean
    public CrearTareaUseCase crearTareaUseCase(TareaRepository r) {
        return new CrearTareaService(r);
    }

    @Bean
    public EditarTareaUseCase editarTareaUseCase(
            TareaRepository r, AsignacionSemanalRepository a) {
        return new EditarTareaService(r, a);
    }

    @Bean
    public EliminarTareaUseCase eliminarTareaUseCase(
            TareaRepository t, AsignacionSemanalRepository a) {
        return new EliminarTareaService(t, a);
    }

    @Bean
    public CambiarEstadoTareaUseCase cambiarEstadoTareaUseCase(
            TareaRepository t, AsignacionSemanalRepository a) {
        return new CambiarEstadoTareaService(t, a);
    }

    @Bean
    public AsignarTareaUseCase asignarTareaUseCase(
            TareaRepository t, AsignacionSemanalRepository a,
            ObtenerMiembrosHogarPort p) {
        return new AsignarTareaService(t, a, p);
    }

    @Bean
    public ListarTareasUseCase listarTareasUseCase(
            TareaRepository t, AsignacionSemanalRepository a) {
        return new ListarTareasService(t, a);
    }

    @Bean
    public FiltrarTareasUseCase filtrarTareasUseCase(
            TareaRepository t, AsignacionSemanalRepository a) {
        return new FiltrarTareasService(t, a);
    }
}
