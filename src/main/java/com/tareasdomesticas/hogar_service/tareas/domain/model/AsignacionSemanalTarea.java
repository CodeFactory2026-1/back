package com.tareasdomesticas.hogar_service.tareas.domain.model;

public class AsignacionSemanalTarea {

    private Long idAsignacion;
    private Long idTarea;
    private Long idUsuarioAsignado; // nullable — null significa excedente
    private EstadoTarea estado;
    private boolean excedente;
    public AsignacionSemanalTarea(Long idAsignacion, Long idTarea, Long idUsuarioAsignado) {
        requireNonNull(idAsignacion, "idAsignacion");
        requireNonNull(idTarea,      "idTarea");
        requireNonNull(idUsuarioAsignado, "idUsuarioAsignado al asignar");
        this.idAsignacion      = idAsignacion;
        this.idTarea           = idTarea;
        this.idUsuarioAsignado = idUsuarioAsignado;
        this.estado            = EstadoTarea.ASIGNADO;
        this.excedente         = false;
    }
    public AsignacionSemanalTarea(Long idAsignacion, Long idTarea) {
        requireNonNull(idAsignacion, "idAsignacion");
        requireNonNull(idTarea,      "idTarea");
        this.idAsignacion      = idAsignacion;
        this.idTarea           = idTarea;
        this.idUsuarioAsignado = null;
        this.estado            = EstadoTarea.PENDIENTE;
        this.excedente         = true;
    }
    public static AsignacionSemanalTarea reconstruir(
            Long idAsignacion, Long idTarea,
            Long idUsuarioAsignado, EstadoTarea estado, boolean excedente) {
        requireNonNull(idAsignacion, "idAsignacion");
        requireNonNull(idTarea,      "idTarea");
        requireNonNull(estado,       "estado");
        AsignacionSemanalTarea ast = new AsignacionSemanalTarea();
        ast.idAsignacion      = idAsignacion;
        ast.idTarea           = idTarea;
        ast.idUsuarioAsignado = idUsuarioAsignado;
        ast.estado            = estado;
        ast.excedente         = excedente;
        return ast;
    }
    private AsignacionSemanalTarea() {}


    public void cambiarEstado(EstadoTarea nuevoEstado) {
        requireNonNull(nuevoEstado, "El nuevo estado");
        if (this.excedente)
            throw new IllegalStateException("La tarea debe ser asignada antes de cambiar su estado.");
        // Una tarea sin responsable (usuario eliminado) no puede avanzar de estado.
        // Solo puede volver a PENDIENTE, que ya es su estado actual.
        if (this.idUsuarioAsignado == null && nuevoEstado != EstadoTarea.PENDIENTE)
            throw new IllegalStateException(
                    "La tarea no puede cambiar de estado porque no tiene un responsable asignado.");
        if (!this.estado.puedeTransicionarA(nuevoEstado))
            throw new IllegalStateException(
                    "Transición no permitida: " + this.estado + " → " + nuevoEstado);
        if (nuevoEstado == EstadoTarea.PENDIENTE)
            this.idUsuarioAsignado = null;
        this.estado = nuevoEstado;
    }

    public void liberarResponsable() {
        this.idUsuarioAsignado = null;
        this.estado = EstadoTarea.PENDIENTE;
    }

    public boolean esPendienteOExcedente() {
        return this.excedente || this.estado == EstadoTarea.PENDIENTE;
    }


    public Long        getIdAsignacion()      { return idAsignacion; }
    public Long        getIdTarea()           { return idTarea; }
    public Long        getIdUsuarioAsignado() { return idUsuarioAsignado; }
    public EstadoTarea getEstado()            { return estado; }
    public boolean     isExcedente()          { return excedente; }


    private static void requireNonNull(Object val, String campo) {
        if (val == null) throw new IllegalArgumentException(campo + " es obligatorio.");
    }
}