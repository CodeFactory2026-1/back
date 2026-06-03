package com.tareasdomesticas.hogar_service.tareas.domain.model;

public class AsignacionSemanalTarea {

    private Long idAsignacion;
    private Long idTarea;
    private Long idUsuarioAsignado;
    private EstadoTarea estado;
    private boolean excedente;
    private Long idUsuarioFinalizador;
    private java.time.LocalDateTime fechaUltimoCambioEstado;

    public AsignacionSemanalTarea(Long idAsignacion, Long idTarea, Long idUsuarioAsignado) {
        requireNonNull(idAsignacion, "idAsignacion");
        requireNonNull(idTarea, "idTarea");
        requireNonNull(idUsuarioAsignado, "idUsuarioAsignado al asignar");
        this.idAsignacion = idAsignacion;
        this.idTarea = idTarea;
        this.idUsuarioAsignado = idUsuarioAsignado;
        this.estado = EstadoTarea.ASIGNADO;
        this.excedente = false;
    }

    public AsignacionSemanalTarea(Long idAsignacion, Long idTarea) {
        requireNonNull(idAsignacion, "idAsignacion");
        requireNonNull(idTarea, "idTarea");
        this.idAsignacion = idAsignacion;
        this.idTarea = idTarea;
        this.idUsuarioAsignado = null;
        this.estado = EstadoTarea.PENDIENTE;
        this.excedente = true;
    }

    public static AsignacionSemanalTarea reconstruir(
            Long idAsignacion, Long idTarea,
            Long idUsuarioAsignado, EstadoTarea estado, boolean excedente) {
        requireNonNull(idAsignacion, "idAsignacion");
        requireNonNull(idTarea, "idTarea");
        requireNonNull(estado, "estado");
        AsignacionSemanalTarea ast = new AsignacionSemanalTarea();
        ast.idAsignacion = idAsignacion;
        ast.idTarea = idTarea;
        ast.idUsuarioAsignado = idUsuarioAsignado;
        ast.estado = estado;
        ast.excedente = excedente;
        return ast;
    }

    public static AsignacionSemanalTarea reconstruir(
            Long idAsignacion, Long idTarea,
            Long idUsuarioAsignado, EstadoTarea estado, boolean excedente,
            Long idUsuarioFinalizador, java.time.LocalDateTime fechaUltimoCambioEstado) {
        AsignacionSemanalTarea ast = reconstruir(idAsignacion, idTarea, idUsuarioAsignado, estado, excedente);
        ast.idUsuarioFinalizador = idUsuarioFinalizador;
        ast.fechaUltimoCambioEstado = fechaUltimoCambioEstado;
        return ast;
    }

    private AsignacionSemanalTarea() {
    }

    public void cambiarEstado(EstadoTarea nuevoEstado) {
        requireNonNull(nuevoEstado, "El nuevo estado");
        if (this.excedente)
            throw new IllegalStateException("La tarea debe ser asignada antes de cambiar su estado.");
        if (this.idUsuarioAsignado == null && nuevoEstado != EstadoTarea.PENDIENTE)
            throw new IllegalStateException(
                    "La tarea no puede cambiar de estado porque no tiene un responsable asignado.");
        if (!this.estado.puedeTransicionarA(nuevoEstado))
            throw new IllegalStateException(
                    "Transición no permitida: " + this.estado + " → " + nuevoEstado);
        if (nuevoEstado == EstadoTarea.PENDIENTE)
            this.idUsuarioAsignado = null;
        this.estado = nuevoEstado;
        // Registrar meta: quién finaliza y cuándo
        this.fechaUltimoCambioEstado = java.time.LocalDateTime.now();
        if (nuevoEstado == EstadoTarea.FINALIZADO) {
            this.idUsuarioFinalizador = this.idUsuarioAsignado;
        } else {
            this.idUsuarioFinalizador = null;
        }
    }

    public void liberarResponsable() {
        this.idUsuarioAsignado = null;
        this.estado = EstadoTarea.PENDIENTE;
        this.idUsuarioFinalizador = null;
        this.fechaUltimoCambioEstado = null;
    }

    public boolean esPendienteOExcedente() {
        return this.excedente || this.estado == EstadoTarea.PENDIENTE;
    }

    public Long getIdAsignacion() {
        return idAsignacion;
    }

    public Long getIdTarea() {
        return idTarea;
    }

    public Long getIdUsuarioAsignado() {
        return idUsuarioAsignado;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public boolean isExcedente() {
        return excedente;
    }

    public Long getIdUsuarioFinalizador() {
        return idUsuarioFinalizador;
    }

    public java.time.LocalDateTime getFechaUltimoCambioEstado() {
        return fechaUltimoCambioEstado;
    }

    private static void requireNonNull(Object val, String campo) {
        if (val == null)
            throw new IllegalArgumentException(campo + " es obligatorio.");
    }
}